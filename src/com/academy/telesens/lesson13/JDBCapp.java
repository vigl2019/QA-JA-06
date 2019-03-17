/*

# Задачи на работу с Базой Данных из Java приложения
=====================================
http://www.javaportal.ru/java/tutorial/tutorialJDBC/
http://docs.oracle.com/javase/tutorial/jdbc
http://dmivic.chat.ru/JDBC/introTOC.doc.html
http://proselyte.net/tutorials/jdbc/
=====================================
1)  Подготовить следующие классы соответствующие БД Телефонной сети
		и сложить их в пакет model:

		a)	Subscriber, который хранит информацию об абоненте:
				поля:
					- private long subscriberId; 		// уникальный идентификатор
					- private String firstName; 	// имя
					- private String lastName;  	// фамилия
					- private int age; 				// возраст
					- private Enum gender;
				методы:
					- set, get // для каждого поля
					- toString();
					- equals();

				реализует интерфейс:
					- Comparable // сравнивает двух абонентов по полю subscriber_id нужно для сортировки)

		b) 	Operator:
				поля:
					- private long operatorId;
					- private String name;
					- private String address;
					- private BigDecimal fee; // aбонплата
					- private List<String> phoneNumbers; // Список телефонных номеров

				методы:
					- set, get // для каждого поля
					- toString();
					- equals(); // сравнивает по operator_id
					- addNumber(); // добавляет новый номер в список

		с) PhoneBookRecord (запись в телефонной книге):

				поля:
						- private String phoneNumber;
						- private Long subscriberId;
						- private Date registeredDate;

				методы:
					- set, get // для каждого поля
					- toString();
					- equals(); // сравнивает по operator_id

2) Создать пакет dao, в котором описать интерфейсы:
	a) SubscriberDao
			методы:
				- boolean save(Subscriber subscriber);
				- boolean remove(Subscriber subscriber);
				- List<Subscriber> getAll();
				- Subscriber findById(long id);

	b) 	OperatorDao
			методы:
				- boolean save(Operator operator);
				- boolean remove(Operator operator);
				- List<Operator> getAll();
				- Operator findById(long id);

	c) PhoneBookDao
			методы:
				- boolean save(PhoneBookRecord record);
				- boolean remove(PhoneBookRecord record);
				- List<PhoneBookRecord> getAll();
				- PhoneBookRecord findByKeys(String phoneNumber, Long operatorId);

3) В пакете dao, создать пакет jdbc, в котором реализовать интерфейсы выше,
	используя JDBC API:
		- SubscriberDaoImpl
		- OperatorDaoImpl
		- PhoneBookDaoImpl

4) Используя реализованный выше DAO слой, наполнить БД телефонной сети, случайными тестовыми данными:
	a) Наполнить таблицу абонентов (200):
		- имена фамилии взять в соответстсвующих файлах со списко имен/фамилий (женских/мужских);
		- возраст генерировать случайно (от 5 до 90, можно использовать Гауссово распределение для этого диапазона)

	b)  Наполнить таблицу operator_number телефонными номерами для каждого оператора по 100000 номеров:
		- Life номера с префиксами: 38063*******, 38093*******, 38073*******
		- Kievstar номера с префиксами: 38097*******, 38067*******, 38098*******
		- Vodafone номера с префиксами: 38050*******, 38066*******, 38095*******

	c) Наполнить таблицу phone_book:
		- 	в случайном порядке брать свободный номер из operator_number
			и привязать к случайно взятому абоненту из таблицы abonent

	d) выполнить запросы из задачи 1 на новом наборе данных

	e) вывести на экран свободные номера по каждому оператору
	f) вывести на экран абонентов, не имеющих мобильного номера

5) Используя DAО слой и возможности StreamAPI вывести на консоль
	следующие данные (кол-во записей ограничить размером 100):

	- всех абонентов заданного мобильного оператора
	- все мобильные номера, выбранного абонента
	- всех абонентов по списку номеров
	- кол-во мобильных номеров операторов
	- оператора с наибольшим кол-во мобильных номеров
	- вывести абонентов, которые принадлежат к разным операторам (напр. Life и Kievstar)
	- вывести абонентов, который принадлежит к мобильному оператору, но не принадлежит ни к одному другому
	- определить, какого оператора предпочитают женщины, какого мужчины
	- вывести всех абонентов возраста от 20 до 40 лет, только двух мобильных операторов, отсортировать по фамилии, кол-во не больше 5
	- вывести кол-во абонентов по всем мобильным операторам от 20 до 40 лет, отсортировать убыванию

*/

package com.academy.telesens.lesson13;

import com.academy.telesens.lesson11.Operator;

public class JDBCapp {

    public static void main(String[] args) {

        Utils utils = new Utils();

/*

4) Используя реализованный выше DAO слой, наполнить БД телефонной сети, случайными тестовыми данными:

        a) Наполнить таблицу абонентов (200):

        - имена фамилии взять в соответстсвующих файлах со списко имен/фамилий (женских/мужских);
        - возраст генерировать случайно (от 5 до 90, можно использовать Гауссово распределение для этого диапазона)
*/

        utils.addSubscribersDataToAbonentTable();

        //================================================================================//

/*

b)  Наполнить таблицу operator_number телефонными номерами для каждого оператора по 100000 номеров:

		- Life номера с префиксами: 38063*******, 38093*******, 38073*******
		- Kievstar номера с префиксами: 38097*******, 38067*******, 38098*******
		- Vodafone номера с префиксами: 38050*******, 38066*******, 38095*******

*/

        utils.addPhoneNumbersToOperatorNumberTable(Operator.Operators.LIFE.toString());
        utils.addPhoneNumbersToOperatorNumberTable(Operator.Operators.KYIVSTAR.toString());
        utils.addPhoneNumbersToOperatorNumberTable(Operator.Operators.VODAFONE.toString());

        //================================================================================//

/*

c) Наполнить таблицу phone_book:

		- 	в случайном порядке брать свободный номер из operator_number
			и привязать к случайно взятому абоненту из таблицы abonent
*/


        utils.addDataToPhoneBookTable();

        //================================================================================//

/*

e) вывести на экран свободные номера по каждому оператору

*/

        utils.getFreePhoneOperatorNumbers();

        //================================================================================//

/*
        f) вывести на экран абонентов, не имеющих мобильного номера
*/

        utils.getSubscribersWithOutPhoneNumber();

    }
}
