class Quest:
    def __init__(self, name, items):
        self.name = name
        self.items = items

    def get_missing_items(self, inventory):
        missing = {}

        for item_name in self.items:
            needed = self.items[item_name]
            have = inventory.get_item_quantity(item_name)

            if have < needed:
                missing[item_name] = needed - have

        return missing

    def can_complete(self, inventory):
        missing = self.get_missing_items(inventory)
        return len(missing) == 0
