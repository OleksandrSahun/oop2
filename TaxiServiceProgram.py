import random

class AbstractPerson:
    def __init__(self, name, phone_number):
        self._name = name
        self._phone_number = phone_number

    @property
    def name(self):
        return self._name

    @property
    def phone_number(self):
        return self._phone_number

    def login(self, phone_number):
        raise NotImplementedError("Метод має бути реалізований у підкласах")

class RideInterface:
    def build_route(self, current_location, destination):
        raise NotImplementedError("Метод має бути реалізований у підкласах")

    def calculate_ride_cost(self, distance):
        raise NotImplementedError("Метод має бути реалізований у підкласах")

    def mark_complete(self):
        raise NotImplementedError("Метод має бути реалізований у підкласах")

class Car:
    def __init__(self, model, license_number):
        self._model = model
        self._license_number = license_number

    @property
    def model(self):
        return self._model

    @property
    def license_number(self):
        return self._license_number

class Client(AbstractPerson):
    def __init__(self, name, phone_number, destination, balance):
        super().__init__(name, phone_number)
        self._destination = destination
        self._balance = balance

    @property
    def destination(self):
        return self._destination

    def request_ride(self, driver, current_location):
        print(f"{self.name} створив замовлення до {self.destination}.")
        driver.accept_order(self, current_location)

    def pay_for_ride(self, amount):
        if self._balance >= amount:
            self._balance -= amount
            print(f"{self.name} сплатив {amount} грн. Залишок: {self._balance} грн.")
            return True
        else:
            print(f"{self.name} не має достатньо коштів.")
            return False

    def login(self, phone_number):
        if self.phone_number == phone_number:
            print(f"{self.name} увійшов як клієнт.")
            return True
        print("Помилка входу.")
        return False

class TaxiDriver(AbstractPerson, RideInterface):
    BASE_FARE = 10.0
    FARE_PER_KM = 5.0

    def __init__(self, name, phone_number, car):
        super().__init__(name, phone_number)
        self._car = car
        self._current_route = None
        self._distance = 0

    def accept_order(self, client, current_location):
        print(f"{self.name} прийняв замовлення від {client.name}.")
        self.build_route(current_location, client.destination)
        self._distance = random.randint(5, 20)
        cost = self.calculate_ride_cost(self._distance)
        print(f"Відстань: {self._distance} км. Вартість: {cost} грн.")
        if client.pay_for_ride(cost):
            self.mark_complete()
        else:
            print("Поїздку не завершено через несплату.")

    def build_route(self, current_location, destination):
        self._current_route = f"{current_location} -> {destination}"
        print(f"{self.name} будує маршрут: {self._current_route}.")

    def calculate_ride_cost(self, distance):
        return self.BASE_FARE + (self.FARE_PER_KM * distance)

    def mark_complete(self):
        print(f"{self.name} завершив поїздку за маршрутом: {self._current_route}.")
        self._current_route = None

    def login(self, phone_number):
        if self.phone_number == phone_number:
            print(f"{self.name} увійшов як водій.")
            return True
        print("Помилка входу.")
        return False

def main():
    car = Car("Toyota Camry", "XYZ123")
    driver = TaxiDriver("Аліса Сміт", "098-765-4321", car)
    if not driver.login("098-765-4321"):
        return

    client = Client("Джон Доу", "123-456-7890", "Центральний парк", 60.00)
    if not client.login("123-456-7890"):
        return

    client.request_ride(driver, "Таймс-Сквер")

if __name__ == "__main__":
    main()
