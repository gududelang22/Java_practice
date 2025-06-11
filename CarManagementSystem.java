package org.example.test;

import java.util.ArrayList;
import java.util.List;

//汽车类
class Car{
    //类的特征(属性/字段)
    private String brand; //品牌
    private String model; //型号
    private int year; //年份
    private double price; //价格
    private String color; //颜色
    private double engineSize; //发动机排量L
    private boolean isSold; //是否已售出

    public boolean isSold() {
        return isSold;
    }
    public String getModel(){
        return model;
    }
    public String getInfo() {
        System.out.println(brand+" "+model+" "+year+" "+color+" "+engineSize+" "+price);
        return String.format("%s %s %d款 | 颜色：%s | 排量：%.1fL | 价格：￥%.2f",brand,model,year,color,engineSize,price);
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
        System.out.println("创建了一辆汽车"+brand+" "+model);
    }
}
//电动车类 继承自汽车类
class ElectricCar extends Car{
    //特有属性
    private double batteryCapacity; //电池容量
    private int range;  //续航里程

    public ElectricCar(String brand, String model, int year, double price, String color, double batteryCapacity,int range) {
        super(brand, model, year, price, color, 0);
        this.batteryCapacity = batteryCapacity;
        this.range = range;
    }
}

//客户类
class Customer{
    //客户属性
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
        System.out.println("新客户注册 "+name);
    }

    public void browseCars(List<Car> cars) {
        System.out.println("\n"+name+"正在浏览汽车：");
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
            System.out.println(name+" 购买了 "+car.getInfo());
        }else{
            System.out.println(name+" 购买失败：预算不足！");
        }
    }

    public void displayOwnedCars() {
        System.out.println("\n"+name+"拥有的汽车：");
        if(ownedCars.isEmpty()){
            System.out.println(" 暂无汽车");
        }else{
            for (Car car : ownedCars) {
                System.out.println(" - "+car.getInfo());
            }
        }
    }
}
//汽车经销商类
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
        System.out.println("汽车经销商创建"+name);
    }
    public void addCar(Car car){
        inventory.add(car);
    }
    public void displayDealerInfo(){
        System.out.println("\n==== 经销商信息 ====");
        System.out.println("名称："+name);
        System.out.println("地址："+address);
        System.out.println("库存车辆："+inventory.size()+"辆");
    }

    public void displayInventory() {
        System.out.println("\n ==== 库存汽车 ====");
        for (Car car:inventory) {
            String status = car.isSold()?"已售出":"在售";
            System.out.println(car.getInfo()+" | 状态 "+status);
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
            salesRecord.add(car.getInfo()+" | 买家："+customer.getName());
            System.out.println("销售成功："+car.getInfo());
        }else{
            System.out.println("销售失败，该车已经售出或预算不够!");
        }
    }

    public void displaySalesReport() {
        System.out.println("\n ==== 销售记录 ====");
        if (salesRecord.isEmpty()){
            System.out.println("暂无销售记录");
        }else{
            for (String record:salesRecord) {
                System.out.println(" - "+record);
            }
        }
    }
}
// 主类 - 程序入口
public class CarManagementSystem {
    public static void main(String[] args) {
        //创建汽车经销商对象
        CarDealer dealer = new CarDealer("极速汽车","北京市朝阳区建国路8888号");
        //添加汽车到经销商库存
        dealer.addCar(new Car("Toyota","Camry",2022,24900,"白色",2.5));
        dealer.addCar(new Car("Honda","Accord",2023,26900,"黑色",2.0));
        dealer.addCar(new Car("Tesla","Model",2023,33900,"红色",0));
        dealer.addCar(new ElectricCar("BYD","han",2023,31900,"蓝色",85.4,605));
        //创建客户
        Customer customer1 = new Customer("张三","13364284",100000);
        Customer customer2 = new Customer("李四","13363244",2500);
        //展示经销商信息
        dealer.displayDealerInfo();
        System.out.print("***************************************\n");
        //展示库存汽车
        dealer.displayInventory();
        System.out.print("***************************************\n");
        //客户浏览汽车
        customer1.browseCars(dealer.getInventory());
        System.out.print("***************************************\n");
        //客户购买汽车
        Car carToBuy = dealer.getCarByModel("Camry");
        if(carToBuy != null){
            dealer.sellCar(carToBuy,customer1);
        }
        System.out.print("***************************************\n");
        //展示销售记录
        dealer.displaySalesReport();
        //展示客户拥有的汽车
        customer1.displayOwnedCars();
    }
}

