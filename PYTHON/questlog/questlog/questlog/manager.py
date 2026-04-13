import json
import os
import shlex

from questlog.quest import Quest
from questlog.inventory import Inventory


class QuestLogManager:
    def __init__(self, config_file="config.json"):
        self.config = self.load_config(config_file)
        self.quests = self.load_quests()
        self.inventory = self.load_inventory()

    def load_config(self, config_file):
        with open(config_file, "r", encoding="utf-8") as file:
            return json.load(file)

    def load_quests(self):
        quest_file = self.config["quest_file"]

        with open(quest_file, "r", encoding="utf-8") as file:
            data = json.load(file)

        quests = []

        for quest_data in data:
            quest = Quest(quest_data["name"], quest_data["items"])
            quests.append(quest)

        return quests

    def load_inventory(self):
        inventory_file = self.config["inventory_file"]

        try:
            with open(inventory_file, "r", encoding="utf-8") as file:
                data = json.load(file)
            return Inventory(data)
        except FileNotFoundError:
            return Inventory({})

    def save_inventory(self):
        inventory_file = self.config["inventory_file"]
        folder = os.path.dirname(inventory_file)

        if folder != "":
            os.makedirs(folder, exist_ok=True)

        with open(inventory_file, "w", encoding="utf-8") as file:
            json.dump(self.inventory.to_dict(), file, indent=2)

    def find_quest(self, quest_name):
        for quest in self.quests:
            if quest.name.lower() == quest_name.lower():
                return quest
        return None

    def quest_list(self):
        for i in range(len(self.quests)):
            print(f"{i + 1}. {self.quests[i].name}")

    def quest_view(self, quest_name):
        quest = self.find_quest(quest_name)

        if quest is None:
            print(f"Error: Quest '{quest_name}' not found.")
            return

        print(f"Quest: {quest.name}")
        print("Required Items:")

        for item_name in quest.items:
            print(f"- {item_name}: {quest.items[item_name]}")

    def quest_gap(self, quest_name):
        quest = self.find_quest(quest_name)

        if quest is None:
            print(f"Error: Quest '{quest_name}' not found.")
            return

        missing = quest.get_missing_items(self.inventory)

        if len(missing) == 0:
            print(f"No missing items for '{quest.name}'.")
            return

        print(f"Missing items for '{quest.name}':")
        for item_name in missing:
            print(f"- {item_name}: {missing[item_name]}")

    def quest_complete(self, quest_name):
        quest = self.find_quest(quest_name)

        if quest is None:
            print(f"Error: Quest '{quest_name}' not found.")
            return

        missing = quest.get_missing_items(self.inventory)

        if len(missing) > 0:
            print(f"Error: Cannot complete '{quest.name}'. Missing items:")
            for item_name in missing:
                print(f"- {item_name}: {missing[item_name]}")
            return

        for item_name in quest.items:
            self.inventory.use_item(item_name, quest.items[item_name])

        self.save_inventory()
        print(f"Successfully completed '{quest.name}'. Inventory has been updated.")

    def inventory_add(self, item_name, quantity_text):
        try:
            quantity = int(quantity_text)
            self.inventory.add_item(item_name, quantity)
            self.save_inventory()
            print(f"Successfully added {quantity} of '{item_name}'.")
        except ValueError as error:
            print(f"Error: {error}")

    def inventory_use(self, item_name, quantity_text):
        try:
            quantity = int(quantity_text)
            self.inventory.use_item(item_name, quantity)
            self.save_inventory()
            print(f"Successfully used {quantity} of '{item_name}'.")
        except ValueError as error:
            print(f"Error: {error}")

    def plan(self):
        found = []

        for quest in self.quests:
            if quest.can_complete(self.inventory):
                found.append(quest.name)

        if len(found) == 0:
            print("No completable quests.")
            return

        print("Completable Quests:")
        for name in found:
            print(f"- {name}")

    def write_report(self, lines):
        report_file = self.config["report_file"]
        folder = os.path.dirname(report_file)

        if folder != "":
            os.makedirs(folder, exist_ok=True)

        with open(report_file, "w", encoding="utf-8") as file:
            for line in lines:
                file.write(line + "\n")

    def inventory_process(self, filepath):
        try:
            with open(filepath, "r", encoding="utf-8") as file:
                commands = file.readlines()
        except FileNotFoundError:
            print(f"Error: Batch file not found: {filepath}")
            return

        report_lines = []

        for raw_line in commands:
            line = raw_line.strip()

            if line == "":
                continue

            parts = line.split()

            if len(parts) != 3:
                report_lines.append(f"ERROR: Invalid command format '{line}'.")
                continue

            command = parts[0].upper()
            item_name = parts[1]

            try:
                quantity = int(parts[2])
                if quantity <= 0:
                    raise ValueError
            except ValueError:
                report_lines.append(f"ERROR: Invalid quantity in '{line}'.")
                continue

            if command == "ADD":
                self.inventory.add_item(item_name, quantity)
                report_lines.append(f"SUCCESS: {line}")
            elif command == "USE":
                try:
                    self.inventory.use_item(item_name, quantity)
                    report_lines.append(f"SUCCESS: {line}")
                except ValueError as error:
                    report_lines.append(f"ERROR: {error}")
            else:
                report_lines.append(f"ERROR: Unknown command '{parts[0]}'.")

        self.save_inventory()
        self.write_report(report_lines)
        print(f"Batch processing complete. See '{self.config['report_file']}' for details.")

    def run_command(self, args):
        try:
            if len(args) == 0:
                print("Error: No command provided.")
                return

            command = args[0].lower()

            if command == "plan":
                self.plan()
                return

            if command == "quest":
                if len(args) < 2:
                    print("Error: Missing quest subcommand.")
                    return

                subcommand = args[1].lower()

                if subcommand == "list":
                    self.quest_list()
                    return

                if subcommand == "view":
                    if len(args) < 3:
                        print("Error: Missing quest name.")
                        return
                    quest_name = " ".join(args[2:])
                    self.quest_view(quest_name)
                    return

                if subcommand == "gap":
                    if len(args) < 3:
                        print("Error: Missing quest name.")
                        return
                    quest_name = " ".join(args[2:])
                    self.quest_gap(quest_name)
                    return

                if subcommand == "complete":
                    if len(args) < 3:
                        print("Error: Missing quest name.")
                        return
                    quest_name = " ".join(args[2:])
                    self.quest_complete(quest_name)
                    return

                print(f"Error: Unknown quest subcommand '{args[1]}'.")
                return

            if command == "inventory":
                if len(args) < 2:
                    print("Error: Missing inventory subcommand.")
                    return

                subcommand = args[1].lower()

                if subcommand == "add":
                    if len(args) != 4:
                        print("Error: Usage: inventory add <item_name> <quantity>")
                        return
                    self.inventory_add(args[2], args[3])
                    return

                if subcommand == "use":
                    if len(args) != 4:
                        print("Error: Usage: inventory use <item_name> <quantity>")
                        return
                    self.inventory_use(args[2], args[3])
                    return

                if subcommand == "process":
                    if len(args) != 3:
                        print("Error: Usage: inventory process <filepath>")
                        return
                    self.inventory_process(args[2])
                    return

                print(f"Error: Unknown inventory subcommand '{args[1]}'.")
                return

            print(f"Error: Unknown command '{args[0]}'.")

        except FileNotFoundError as error:
            print(f"Error: {error}")
        except json.JSONDecodeError:
            print("Error: JSON file format is invalid.")
        except Exception as error:
            print(f"Error: {error}")

    def manage(self):
        print("QuestLog Manager")
        print("Type 'exit' to quit.")

        while True:
            try:
                user_input = input("> ").strip()

                if user_input == "":
                    continue

                if user_input.lower() == "exit":
                    self.save_inventory()
                    print("Inventory saved. Goodbye!")
                    break

                args = shlex.split(user_input)
                self.run_command(args)

            except KeyboardInterrupt:
                self.save_inventory()
                print("\nInventory saved. Goodbye!")
                break
            except EOFError:
                self.save_inventory()
                print("\nInventory saved. Goodbye!")
                break
            except ValueError as error:
                print(f"Error: {error}")
