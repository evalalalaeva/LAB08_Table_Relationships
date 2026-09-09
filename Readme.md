# 🛍️ Lab 8: Table Relationships — Product Shop

**นักศึกษา:** 673380286-0 &middot; Section 2
**วิชา:** CP353002 Principles of Software Design
**เรื่อง:** ความสัมพันธ์ตาราง 1:1 และ 1:N ด้วย Spring Boot + JPA + PostgreSQL

## โครงสร้างโปรเจกต์

```
src/main/java/com/example/demo/
├── DemoApplication.java
├── model/
│   ├── Product.java              ← @OneToOne → ProductDetail, @OneToMany → List<Review>
│   ├── ProductDetail.java        ← @OneToOne(mappedBy)
│   └── Review.java               ← @ManyToOne → Product (FK: product_id)
├── repository/
│   ├── ProductRepository.java
│   ├── ProductDetailRepository.java
│   └── ReviewRepository.java
├── strategy/
│   ├── DiscountStrategy.java
│   ├── NoDiscountStrategy.java
│   ├── MemberDiscountStrategy.java    (ลด 10%)
│   ├── SeasonalSaleStrategy.java      (ลด 20%)
│   └── DiscountContext.java
├── service/
│   └── ProductService.java
└── controller/
    └── ProductController.java

src/main/resources/
├── application.properties
├── static/css/style.css
└── templates/products/{list,add,edit,delete}.html
```

**เวอร์ชัน:** Spring Boot 3.5.11 (Jakarta EE namespace, Java 17) — อัปเดตจาก 2.7.x เดิมซึ่งหมด OSS support ไปตั้งแต่ 30 มิ.ย. 2023

## วิธีรัน

1. ติดตั้ง PostgreSQL แล้วสร้างฐานข้อมูล:
   ```sql
   CREATE DATABASE lab8shop;
   ```
2. แก้ `src/main/resources/application.properties` ใส่รหัสผ่าน postgres ของคุณ
3. รันด้วย Maven:
   ```
   mvn spring-boot:run
   ```
4. เปิดเบราว์เซอร์ไปที่ <http://localhost:8080/products>

Spring JPA จะสร้างตาราง `products`, `product_details`, `reviews` พร้อม Foreign Key ให้อัตโนมัติ
(`spring.jpa.hibernate.ddl-auto=update`).

## URL Mappings

| Method | URL                     | หน้าที่                        |
| ------ | ----------------------- | ------------------------------ |
| GET    | `/products`             | รายการสินค้า                   |
| GET    | `/products/add`         | ฟอร์มเพิ่มสินค้า               |
| POST   | `/products/save`        | บันทึกสินค้า (+ ProductDetail) |
| GET    | `/products/edit/{id}`   | ฟอร์มแก้ไข                     |
| POST   | `/products/update/{id}` | อัปเดต                         |
| GET    | `/products/delete/{id}` | ยืนยันลบ                       |
| POST   | `/products/delete/{id}` | ลบสินค้า                       |

## หมายเหตุ

- ตั้งชื่อสินค้าตัวอย่าง เช่น `iPhone 15 Pro (673380286-0 SEC 2)` ตามที่โจทย์กำหนด ก่อนถ่ายภาพหน้าจอส่งงาน
- ดู annotation ความสัมพันธ์ 1:1 ที่ `Product.detail` (owner, มี `detail_id`) และ `ProductDetail.product` (mappedBy)
- ดู annotation ความสัมพันธ์ 1:N ที่ `Review.product` (owner, มี `product_id`) และ `Product.reviews` (mappedBy)
- Strategy Pattern อยู่ที่แพ็กเกจ `strategy/` — `DiscountContext` เลือก strategy จาก `Product.discountType` ตอน runtime
