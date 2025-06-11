package org.example.test;

import java.util.ArrayList;
import java.util.List;

//������
class Car{
    //�������(����/�ֶ�)
    private String brand; //Ʒ��
    private String model; //�ͺ�
    private int year; //���
    private double price; //�۸�
    private String color; //��ɫ
    private double engineSize; //����������L
    private boolean isSold; //�Ƿ����۳�

    public boolean isSold() {
        return isSold;
    }
    public String getModel(){
        return model;
    }
    public String getInfo() {
        System.out.println(brand+" "+model+" "+year+" "+color+" "+engineSize+" "+price);
        return String.format("%s %s %d�� | ��ɫ��%s | ������%.1fL | �۸񣺣�%.2f",brand,model,year,color,engineSize,price);
    }
    public void markAsSold() {
        isSold = true;
    }
    public double getPrice(){
        return price;
    }
    public Car(String brand, String model, int year, double price, String color, double engineSize) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.color = color;
        this.engineSize = engineSize;
        this.isSold = false;
        System.out.println("������һ������"+brand+" "+model);
    }
}
//�綯���� �̳���������
class ElectricCar extends Car{
    //��������
    private double batteryCapacity; //�������
    private int range;  //�������

    public ElectricCar(String brand, String model, int year, double price, String color, double batteryCapacity,int range) {
        super(brand, model, year, price, color, 0);
        this.batteryCapacity = batteryCapacity;
        this.range = range;
    }
}

//�ͻ���
class Customer{
    //�ͻ�����
    private String name;
    private String phone;
    private double budget;
    private List<Car> ownedCars;
    public String getName() {
        return name;
    }
    public String getPhone() {
        return phone;
    }
    public double getBudget() {
        return budget;
    }
    public List<Car> getOwnedCars() {
        return ownedCars;
    }
    public Customer(String name, String phone, double budget) {
        this.name = name;
        this.phone = phone;
        this.budget = budget;
        this.ownedCars = new ArrayList<>();
        System.out.println("�¿ͻ�ע�� "+name);
    }

    public void browseCars(List<Car> cars) {
        System.out.println("\n"+name+"�������������");
        for(Car car : cars){
            if (!car.isSold()){
                System.out.println(" - "+car.getInfo());
            }
        }
    }

    public void buyCar(Car car) {
        if(car.getPrice()<=budget){
            ownedCars.add(car);
            budget-= car.getPrice();
            System.out.println(name+" ������ "+car.getInfo());
        }else{
            System.out.println(name+" ����ʧ�ܣ�Ԥ�㲻�㣡");
        }
    }

    public void displayOwnedCars() {
        System.out.println("\n"+name+"ӵ�е�������");
        if(ownedCars.isEmpty()){
            System.out.println(" ��������");
        }else{
            for (Car car : ownedCars) {
                System.out.println(" - "+car.getInfo());
            }
        }
    }
}
//������������
class CarDealer{
    private String name;
    private String address;
    private List<Car> inventory;
    private List<String> salesRecord;

    public CarDealer(String name,String address){
        this.name = name;
        this.address = address;
        this.inventory = new ArrayList<>();
        this.salesRecord = new ArrayList<>();
        System.out.println("���������̴���"+name);
    }
    public void addCar(Car car){
        inventory.add(car);
    }
    public void displayDealerInfo(){
        System.out.println("\n==== ��������Ϣ ====");
        System.out.println("���ƣ�"+name);
        System.out.println("��ַ��"+address);
        System.out.println("��泵����"+inventory.size()+"��");
    }

    public void displayInventory() {
        System.out.println("\n ==== ������� ====");
        for (Car car:inventory) {
            String status = car.isSold()?"���۳�":"����";
            System.out.println(car.getInfo()+" | ״̬ "+status);
        }
    }

    public List<Car> getInventory() {
        return inventory;
    }

    public Car getCarByModel(String model) {
        for (Car car: inventory) {
            if(!car.isSold()&&car.getModel().equals(model)){
                return car;
            }
        }
        return null;
    }

    public void sellCar(Car car, Customer customer) {
        if (!car.isSold()&&car.getPrice()<=customer.getBudget()){
            car.markAsSold();
            customer.buyCar(car);
            salesRecord.add(car.getInfo()+" | ��ң�"+customer.getName());
            System.out.println("���۳ɹ���"+car.getInfo());
        }else{
            System.out.println("����ʧ�ܣ��ó��Ѿ��۳���Ԥ�㲻��!");
        }
    }

    public void displaySalesReport() {
        System.out.println("\n ==== ���ۼ�¼ ====");
        if (salesRecord.isEmpty()){
            System.out.println("�������ۼ�¼");
        }else{
            for (String record:salesRecord) {
                System.out.println(" - "+record);
            }
        }
    }
}
// ���� - �������
public class CarManagementSystem {
    public static void main(String[] args) {
        //�������������̶���
        CarDealer dealer = new CarDealer("��������","�����г���������·8888��");
        //��������������̿��
        dealer.addCar(new Car("Toyota","Camry",2022,24900,"��ɫ",2.5));
        dealer.addCar(new Car("Honda","Accord",2023,26900,"��ɫ",2.0));
        dealer.addCar(new Car("Tesla","Model",2023,33900,"��ɫ",0));
        dealer.addCar(new ElectricCar("BYD","han",2023,31900,"��ɫ",85.4,605));
        //�����ͻ�
        Customer customer1 = new Customer("����","13364284",100000);
        Customer customer2 = new Customer("����","13363244",2500);
        //չʾ��������Ϣ
        dealer.displayDealerInfo();
        System.out.print("***************************************\n");
        //չʾ�������
        dealer.displayInventory();
        System.out.print("***************************************\n");
        //�ͻ��������
        customer1.browseCars(dealer.getInventory());
        System.out.print("***************************************\n");
        //�ͻ���������
        Car carToBuy = dealer.getCarByModel("Camry");
        if(carToBuy != null){
            dealer.sellCar(carToBuy,customer1);
        }
        System.out.print("***************************************\n");
        //չʾ���ۼ�¼
        dealer.displaySalesReport();
        //չʾ�ͻ�ӵ�е�����
        customer1.displayOwnedCars();
    }
}

