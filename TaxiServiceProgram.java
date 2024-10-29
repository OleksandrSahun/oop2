import java.util.Random;

// Абстрактний клас AbstractPerson
abstract class AbstractPerson {
    private String name;
    private String phoneNumber;

    public AbstractPerson(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Абстрактний метод для логіну
    public abstract boolean login(String phoneNumber);
}

// Інтерфейс RideInterface
interface RideInterface {
    void buildRoute(String currentLocation, String destination);
    double calculateRideCost(double distance);
    void markComplete();
}

// Клас автомобіля
class Car {
    private String model;
    private String licenseNumber;

    public Car(String model, String licenseNumber) {
        this.model = model;
        this.licenseNumber = licenseNumber;
    }

    public String getModel() {
        return model;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }
}

// Клас клієнта, наслідує AbstractPerson
class Client extends AbstractPerson {
    private String destination;
    private double balance;

    public Client(String name, String phoneNumber, String destination, double balance) {
        super(name, phoneNumber);
        this.destination = destination;
        this.balance = balance;
    }

    public String getDestination() {
        return destination;
    }

    // Публікація замовлення
    public void requestRide(TaxiDriver driver, String currentLocation) {
        System.out.println(getName() + " опублікував замовлення до " + destination + ".");
        driver.acceptOrder(this, currentLocation);
    }

    // Оплата поїздки
    public boolean payForRide(double amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println(getName() + " оплатив " + amount + " грн. Залишок на рахунку: " + balance + " грн.");
            return true;
        } else {
            System.out.println(getName() + " не може оплатити поїздку. Недостатньо коштів.");
            return false;
        }
    }

    @Override
    public boolean login(String phoneNumber) {
        if (getPhoneNumber().equals(phoneNumber)) {
            System.out.println(getName() + " успішно увійшов у систему як клієнт.");
            return true;
        }
        System.out.println("Помилка входу.");
        return false;
    }
}

// Клас водія таксі, наслідує AbstractPerson та реалізує інтерфейс RideInterface
class TaxiDriver extends AbstractPerson implements RideInterface {
    private Car car;
    private String currentRoute;
    private double distance;

    private static final double BASE_FARE = 10.0; // Базова плата
    private static final double FARE_PER_KM = 5.0; // Ціна за кілометр

    public TaxiDriver(String name, String phoneNumber, Car car) {
        super(name, phoneNumber);
        this.car = car;
    }

    // Прийняття замовлення
    public void acceptOrder(Client client, String currentLocation) {
        System.out.println(getName() + " прийняв замовлення від " + client.getName() + ".");
        buildRoute(currentLocation, client.getDestination());
        distance = new Random().nextInt(16) + 5; // Генерація відстані 5-20 км для прикладу
        double cost = calculateRideCost(distance);
        System.out.println("Відстань: " + distance + " км. Вартість поїздки: " + cost + " грн.");

        // Платіж
        if (client.payForRide(cost)) {
            markComplete();
        } else {
            System.out.println("Поїздку не завершено через несплату.");
        }
    }

    // Побудова маршруту
    @Override
    public void buildRoute(String currentLocation, String destination) {
        currentRoute = currentLocation + " -> " + destination;
        System.out.println(getName() + " будує маршрут: " + currentRoute + ".");
    }

    // Розрахунок вартості поїздки
    @Override
    public double calculateRideCost(double distance) {
        return BASE_FARE + (FARE_PER_KM * distance);
    }

    // Завершення поїздки
    @Override
    public void markComplete() {
        System.out.println(getName() + " завершив поїздку за маршрутом: " + currentRoute + ".");
        currentRoute = null;
    }

    @Override
    public boolean login(String phoneNumber) {
        if (getPhoneNumber().equals(phoneNumber)) {
            System.out.println(getName() + " успішно увійшов у систему як водій.");
            return true;
        }
        System.out.println("Помилка входу.");
        return false;
    }
}

// Основний клас програми
public class TaxiServiceProgram {
    public static void main(String[] args) {
        // Створюємо машину
        Car car = new Car("Toyota Camry", "XYZ123");

        // Створюємо водія
        TaxiDriver driver = new TaxiDriver("Аліса Сміт", "098-765-4321", car);
        if (!driver.login("098-765-4321")) return;

        // Створюємо клієнта
        Client client = new Client("Джон Доу", "123-456-7890", "Центральний парк", 60.00);
        if (!client.login("123-456-7890")) return;

        // Клієнт запитує поїздку
        client.requestRide(driver, "Таймс-Сквер");
    }
}
