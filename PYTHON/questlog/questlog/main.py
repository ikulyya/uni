import sys
from questlog.manager import QuestLogManager


def main():
    try:
        manager = QuestLogManager()
    except Exception as error:
        print(f"Error: {error}")
        return

    if len(sys.argv) < 2:
        print("Usage: python main.py <command>")
        print("Use 'python main.py manage' for interactive mode.")
        return

    if sys.argv[1].strip().lower() == "manage":
        manager.manage()
    else:
        manager.run_command(sys.argv[1:])


if __name__ == "__main__":
    main()


# ```
# python main.py plan
# python main.py quest list
# python main.py quest view "Goblin Camp"
# python main.py quest gap "Dragon Slayer"
# python main.py quest complete "Goblin Camp"
# python main.py inventory add potion 2
# python main.py inventory use potion 1
# python main.py inventory process batch_commands.txt
# python main.py manage
# ```