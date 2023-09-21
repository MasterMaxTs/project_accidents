# job4j_accidents
#### Сервис "Сайт_Дистанционное урегулирование дорожно-транспортных происшествий"

[![Build Status](https://app.travis-ci.com/MasterMaxTs/project_accidents.svg?branch=master)](https://app.travis-ci.com/MasterMaxTs/project_accidents)


![](https://img.shields.io/badge/java-11-4AB197)&nbsp;&nbsp;&nbsp;<br>
![](https://img.shields.io/badge/maven-3.6.3-4AB197)&nbsp;&nbsp;&nbsp;<br>
![](https://img.shields.io/badge/maven--checkstyle--plugin-3.1.2-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/liquibase--maven--plugin-4.20.0-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/jacoco--maven--plugin-0.8.8-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/maven--javadoc--plugin-3.2.0-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/maven--war--plugin-3.4.0-4AB197)&nbsp;&nbsp;&nbsp;<br>
![](https://img.shields.io/badge/spring--boot--starter--web-2.7.8-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/spring--boot--starter--thymeleaf-2.7.8-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/spring--jdbc-5.3.25-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/spring--data--jpa-2.7.4-4AB197)&nbsp;&nbsp;&nbsp;<br>
![](https://img.shields.io/badge/spring--orm-5.3.25-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/spring--security--web-5.7.6-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/hibenate--core-5.6.11-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/lombok-1.18.26-4AB197)&nbsp;&nbsp;&nbsp;<br>
![](https://img.shields.io/badge/Style:_bootstrap-5.0.2-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/Style:_html-5-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/Style:_css-3-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/DBMS:_PostgreSQL-14.0-4AB197)&nbsp;&nbsp;&nbsp;<br>
![](https://img.shields.io/badge/Test:_spring--boot--starter--test-2.7.8-4AB197)&nbsp;&nbsp;&nbsp;
![](https://img.shields.io/badge/Test:_h2database-2.1.214-4AB197)&nbsp;&nbsp;&nbsp;<br><br>
![](https://img.shields.io/badge/Package:-.war-4AB197)&nbsp;&nbsp;&nbsp;


### Это проект по созданию сайта "Автонарушители", доступного в браузере.
В системе существуют две роли: _Обычные пользователи_ и _Автоинспекторы_.

    Прежде чем пользоваться функционалом приложения, обычному пользователю необходимо пройти процедуру регистрации,
    включающую в себя заполнение аутентификационных данных и карточек учёта собственных транспортных средств:
        
        > username
        > password
        > email
        > Имя
        > Фамилия
        > Регистрационный знак автомобиля
        > Номер свидетельства регистрации автомобиля
        > VIN - код автомобиля

<br>

---
#### Данный проект позволит зарегистрированным пользователям в роли _Обычный пользователь_:
1. Создавать электронные заявки с описанием дорожно-транспортного происшествия (ДТП):
<br><br>
    > В заявлении пользователь указывает:
    > * государственный регистрационный знак (ГРЗ) собственного транспортного средства;
    > * краткое название ДТП;
    > * вид ДТП по типу столкновения;
    > * детальное описание ДТП с приложенными фотографиями;
    > * адрес ДТП.

<br>

2. Отправлять заполнененную электронную заявку на рассмотрение автоинспектору(ам)

<br>

3. Отслеживать в реальном времени статус сопровождения электронной заявки:
<br><br>

   > * <em>"Принят"</em>&emsp;&emsp;&emsp;&emsp;&emsp; &emsp;&emsp;cоздание пользователем заявки в приложении
   > * <em>"Рассматривается"</em>  &emsp;&nbsp;&nbsp;&nbsp;&nbsp;рассмотрение автоинспектором заявки пользователя
   > * <em>"Возвращён"</em>&emsp;&emsp;&emsp;&nbsp;&nbsp; &emsp;&emsp;возврат автоинспектором заявки пользователю 
   > * <em>"Скорректирован"</em>&nbsp;&nbsp;&emsp;&emsp;отредактированная пользователем возвращённая заявка
   > * <em>"Решён"</em>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;решение автоинспектором заявки пользователя
   > * <em>"Архив"</em>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;добавление автоинспектором решённой заявки пользователя в архив
   > * <em>"Ожидание"</em>&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;приостановка автоинспектором рассмотрения заявки пользователя

<br>

4. Отслеживать и читать в реальном времени уведомления от автоинспектора(ов) в процессе рассмотрения заявки:
<br><br>
   > * ![img.png](img/bell_icon.png)&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; перевод заявки в статус <em>"Решён"</em>
   > * ![img.png](img/bell_icon.png)![img.png](img/bell_icon.png)&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; перевод заявки в статус <em>"Ожидание"</em>
   > * ![img.png](img/bell_icon.png)![img.png](img/bell_icon.png)![img.png](img/bell_icon.png)&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; перевод заявки в статус <em>"Возвращён"</em>

<br>

___
#### Данный проект позволит зарегистрированным пользователям в роли _Автоинспектор_:
1. Рассматривать, переводить в ожидание, возвращать, завершать, архивировать и удалять электронные заявки от пользователей

   > При <ins>приостановке</ins> рассмотрения заявки автоинспектор может (по усмотрению) отправить текстовое уведомление пользователю
   >
   > При <ins>завершении</ins> заявки автоинспектором в заявлении заполняются поля:
    >    > * перечисляются применённые статьи к ДТП;
    >    > * дополняются фотографии с места ДТП (не обязательно);
    >    > * выносится постановление
   >
    > При <ins>возврате</ins> заявки пользователю автоинспектор должен описать причину возврата в предоставляемой форме
    >
   > <ins>Удаление</ins> заявки пользователя возможно, если заявка имеет статус сопровождения <em>"Архив"</em>:
     >    >* доступно удаление одной архивной заявки;
     >    >* доступно удаление всех архивных заявок (очистка архива);

<br>

2. Выполнять фильтрацию электронных заявок на основе фильтров и поиска заявок по ГРЗ автомобиля

    > Для быстрого доступа к элементам навигационного меню, связанных с обработкой заявок, на начальной странице и страницах с отфильтрованными заявками созданы дополнительные панели ссылок:
     >    > * Главная страница:<br>![img.png](img/admin_index_links_panel.JPG)<br><br>
     >    > * Страница фильтрации:<br>![img.png](img/admin_filter_links_panel.JPG)<br><br>
     >    > * Страница ожидающих решения заявок:<br>![img.png](img/admin_filter_queue_links_panel.JPG)<br><br>
     >    > * Страница возвращённых заявок:<br>![img.png](img/admin_filter_return_links_panel.JPG)

<br>

3. Администрировать пользователей приложения:
- просматривать профиль;
- изменять доступ пользователю в приложение: разрешать, блокировать;
- выполнять поиск пользователя по username

<br>

---
### Стек технологий

- Java 11
- Spring-boot-starter-web v.2.7.8.
- Spring-boot-starter-thymeleaf v.2.7.8.
- Spring-data-jpa v.2.7.4
- Spring-jdbc v.5.3.25
- Spring-orm v.5.3.25
- Spring-security v.5.7.6
- Hibernate-core v.5.6.11.
- Bootstrap v.5.0.2.

- Lombok v.1.18.26.
- СУБД: PostgreSQL v.14.0.
<br><br>
- Тестирование:
  - Spring-boot-starter-test v.2.7.8.
  - БД: h2database v.2.1.214

<br>

- Упаковка проекта: Web Archive (.war)

---
### Требования к окружению
- Java 11
- Maven v.3.6.3
- PostgreSQL v.14.0

<br>

---
### Запуск проекта
1. Установить СУБД PostgreSQL


2. Создать базу данных с именем accidents:<br>
   ```create database accidents;```


3. Скачать файлы проекта с github по ссылке и разархивировать в выбранную директорию:<br>
   [https://github.com/MasterMaxTs/project_accidents/archive](https://github.com/MasterMaxTs/project_accidents/archive/refs/heads/master.zip)


4. Перейти в директорию проекта, открыть командную строку.</br>
 - Для <ins>первого</ins> запуска приложения выполнить последовательно команды:
     - ```mvn install```
     - ```java -jar target/accidents-1.0.war```
     - внизу окна командной строки скопировать в буфер обмена url:
         <br>http://localhost:8080/index


 - Для <ins>последующего</ins> запуска приложения выполнять команду:
     - ```java -jar target/accidents-1.0.war```
     - внизу окна командной строки скопировать в буфер обмена url:
         <br>http://localhost:8080/index

       
5. Вставить из буфера обмена url в адресную строку браузера:<br>
   [http://localhost:8080/index](http://localhost:8080/index)


6. В базу данных пользователей сайта добавлена одна учётная запись пользователя в роли Администратор.

   > администратору сайта необходимо выполнить вход в приложение со следующими учётными данными и сменить пароль через
    вкладку <Профиль> навигационного меню.
   > * логин: _root_
   > * пароль: _secret_


7. Документацию к проекту можно посмотреть в директории проекта в файле:<br>
    ```target/site/apidocs/index.html```

<br>

---
### Закрытие проекта
Закройте окно командной строки либо:
1. Запишите id процесса (PID) в логах из командной строки:
   ![img.png](img/stop_app_pid.png)
2. Через диспетчер устройств выполните завершение процесса c этим PID


<br>

---
### Взаимодействие с приложением

1. Вид страницы аутентификации/регистрации пользователя

![img.png](img/login_page.JPG)

<br>

2. Вид страницы при не успешной аутентификации пользователя

![img.png](img/login_error_page.JPG)

<br>

3. Вид страницы регистрации нового пользователя

![img.png](img/registration_page.JPG)

<br>

4. Вид страницы при не успешной регистрации нового пользователя

![img.png](img/registration_error_page.JPG)

<br>

5. Вид страницы при успешной регистрации нового пользователя

![img.png](img/registration_success_page.JPG)

<br>

6. Обычный пользователь - Вид главной страницы

![img.png](img/user_index_page.JPG)

<br>

7. Обычный пользователь - Вид страницы профиля и страниц редактирования профиля

![img.png](img/user_edit_profile_pages.jpg)

<br>

8. Обычный пользователь - Вид страницы для создания новой заявки

![img.png](img/user_create_accident_page.JPG)

<br>

9. Обычный пользователь - Вид страницы при успешном создании заявки

![img.png](img/user_success_create_accident_page.JPG)

<br>

10. Обычный пользователь - Вид главной страницы с созданной заявкой и страницы просмотра карточки учёта ТС

![img.png](img/user_index_and_regcard_page.JPG)

<br>

11. Обычный пользователь - Вид главной страницы с созданной заявкой и страницы просмотра заявки

![img.png](img/user_index_and_show_accident_page.JPG)

<br>

12. Обычный пользователь - Вид страницы редактирования заявки

![img.png](img/user_edit_accident.JPG)

<br>

13. Обычный пользователь - Вид страницы, при невозможности редактирования заявки

![img.png](img/user_error_edit_accident_page.JPG)

<br>

14. Администратор приложения - Вид главной страницы с раннее созданной пользовательской заявкой

![img.png](img/admin_index_page.JPG)

<br>

15. Администратор приложения - Вид страниц при решении заявки

![img.png](img/admin_edit_accident_to_resolve.JPG)

<br>

16. Обычный пользователь - Вид главной страницы с решённой заявкой

![img.png](img/user_show_resolved_accident_page.JPG)

<br>

17. Администратор приложения - Вид страниц при переводе заявки в архив

![img.png](img/admin_add_accident_to_archive.JPG)

<br>

18. Администратор приложения - Вид страниц при удалении заявки из архива

![img.png](img/admin_remove_accident_from_archive.JPG)

<br>

19. Администратор приложения - Вид страниц при переводе заявки в ожидание

![img.png](img/admin_edit_accident_to_queue.JPG)

<br>

20. Обычный пользователь - Вид главной страницы с заявкой, ожидающей решения

![img.png](img/user_show_queued_accident_page.JPG)

<br>

21. Администратор приложения - Вид страниц при возврате заявки

![img.png](img/admin_edit_accident_to_return.JPG)

<br>

22. Обычный пользователь - Вид главной страницы с возвращённой заявкой

![img.png](img/user_show_adjusted_accident_page.JPG)

<br>

23. Администратор приложения - Вид страниц администрирования пользователя и страницы со всеми пользователями

![img.png](img/admin_all_users.JPG)

<br>

24. Администратор приложения - Вид страницы успешного поиска пользователя по его username

![img.png](img/admin_successfully_found_user_from_all_users_page.JPG)

<br>

25. Администратор приложения - Вид страницы неуспешного поиска пользователя по его username

![img.png](img/admin_no_found_user_from_all_users_page.JPG)

<br>

26. Администратор приложения - Вид главной страницы со всеми заявками пользователей

![img.png](img/admin_all_accidents_page.JPG)

<br>

27. Администратор приложения - Вид страницы успешного поиска заявок по регистрационному знаку автомобиля

![img.png](img/admin_successfull_found_accidents_by_carplate.JPG)

<br>

28. Администратор приложения - Вид страницы не успешного поиска заявок по регистрационному знаку автомобиля

![img.png](img/admin_successfull_no_found_accidents_by_carplate.JPG)

<br>

29. Администратор приложения - Вид страниц при фильтрации заявок из раздела Фильтр в навигационном меню 

![img.png](img/admin_filter1_accidents_page.JPG)

![img.png](img/admin_filter2_accidents_page.JPG)

![img.png](img/admin_filter3_accidents_page.JPG)

<br>

30. Администратор приложения - Вид страниц при удалении всех архивных заявок (очистка архива)

![img.png](img/admin_remove_all_archived_accidents.JPG)

<br>

---
### Особенности технической реализации учебного проекта

<br>

1. <ins><b>Слой репозитория</b></ins>
  - 1.1. Репозиторий Автомобильных инцидентов:

| Интерфейс                          | Реализации	                    | Описание	                                      |
|:-----------------------------------|:-------------------------------|:-----------------------------------------------|
| AccidentRepository                 | HibernateAccidentRepository    | доступ к хранилищу в БД с помощью Hibernate    |
| AccidentRepository                 | JdbcTemplateAccidentRepository | доступ к хранилищу в БД с помощью JdbcTemplate |
| AccidentRepository                 | MemAccidentRepository          | доступ к хранилищу в памяти                    |
| AccidentPagingAndSortingRepository | -                              | доступ к хранилищу в БД с помощью Spring Data  |

<br>

  - 1.2. Репозиторий Cопроводительных документов:

| Интерфейс               | Реализации	            | Описание	                                     |
|:------------------------|:-----------------------|:----------------------------------------------|
| DocumentRepository      | MemDocumentRepository  | доступ к хранилищу в памяти                   |
| DocumentCrudRepository  | -                      | доступ к хранилищу в БД с помощью Spring Data |


<br>
<br>

2. <ins><b>Слой сервиса</b></ins>
  - 2.1. Сервис Автомобильных инцидентов:

| Интерфейс               | Реализации	            | Описание	                                                                            |
|:------------------------|:-----------------------|:-------------------------------------------------------------------------------------|
| AccidentService         | AccidentServiceImpl    | сервис автоинцидентов с внедрением зависимости от AccidentRepository                 |
| -                       | AccidentDataService    | сервис автоинцидентов с внедрением зависимости от AccidentPagingAndSortingRepository |

<br>

  - 2.2. Сервис Сопроводительных документов:

| Интерфейс               | Реализации	            | Описание	                                                                             |
|:------------------------|:-----------------------|:--------------------------------------------------------------------------------------|
| DocumentService         | DocumentServiceImpl    | сервис cопроводительных документов с внедрением зависимости от MemDocumentRepository  |
| -                       | DocumentDataService    | сервис cопроводительных документов с внедрением зависимости от DocumentCrudRepository |


<br>
<br>

3. <ins><b>Профили сборки проекта и файлы конфигурации приложения</b></ins>

 | Профиль         |            Файл 	            |                Файл  	                |
|:----------------|:----------------------------:|:-------------------------------------:|
| Тестовый (test) | db/liquibase_test.properties | resources/application-test.properties |
| Продакшн (prod) |  db/liquibase.properties  	  | resources/application-prod.properties |


<br>
<br>

4. <ins><b>Виды главной страницы приложения</ins></b>

| Роль пользователя |     Имя вида      |  
|:------------------|:-----------------:|
| ROLE_USER         | user/index/index  | 
| ROLE_ADMIN        | admin/index/index | 


<br>

---
### Контакты
* email: max86ts@gmail.com
* telegram: matsurkanov