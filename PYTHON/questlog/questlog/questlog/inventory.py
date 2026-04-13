class Inventory:
    def __init__(self, items=None):
        if items is None:
            self.items = {}
        else:
            self.items = items

    def get_item_quantity(self, item_name):
        if item_name in self.items:
            return self.items[item_name]
        return 0

    def add_item(self, item_name, quantity):
        if quantity <= 0:
            raise ValueError("Quantity must be a positive integer.")

        current = self.get_item_quantity(item_name)
        self.items[item_name] = current + quantity

    def use_item(self, item_name, quantity):
        if quantity <= 0:
            raise ValueError("Quantity must be a positive integer.")

        current = self.get_item_quantity(item_name)

        if current < quantity:
            raise ValueError(f"Cannot USE {quantity} {item_name}, only {current} available.")

        self.items[item_name] = current - quantity

    def to_dict(self):
        return self.items
