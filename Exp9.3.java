import javax.persistence.*;

Entity
public class Account { @Id
private int accountId; private String holderName; private double balance;

// Constructors, getters, setters
}

import javax.persistence.*; import java.util.Date;

@Entity
public class BankTransaction { @Id
@GeneratedValue(strategy = GenerationType.IDENTITY) private int txnId;
private int fromAcc; private int toAcc; private double amount;
private Date txnDate = new Date();

// Constructors, getters, setters
}
import org.hibernate.*;
import org.springframework.transaction.annotation.Transactional;
public class BankService {
private SessionFactory sessionFactory;

public BankService(SessionFactory sessionFactory) { this.sessionFactory = sessionFactory;
}

@Transactional
public void transferMoney(int fromId, int toId, double amount) { Session session = sessionFactory.getCurrentSession();

Account from = session.get(Account.class, fromId); Account to = session.get(Account.class, toId);

if (from.getBalance() < amount) {
throw new RuntimeException("Insufficient Balance");
}

from.setBalance(from.getBalance() - amount); to.setBalance(to.getBalance() + amount);

session.update(from); session.update(to);

BankTransaction txn = new BankTransaction(fromId, toId, amount); session.save(txn);
}
}
@Configuration @EnableTransactionManagement public class AppConfig {
@Bean
public DataSource dataSource() {
DriverManagerDataSource ds = new DriverManagerDataSource(); ds.setDriverClassName("com.mysql.cj.jdbc.Driver"); ds.setUrl("jdbc:mysql://localhost:3306/testdb"); ds.setUsername("root");
ds.setPassword("password");
return ds;
}

@Bean
public LocalSessionFactoryBean sessionFactory() { LocalSessionFactoryBean lsf = new LocalSessionFactoryBean(); lsf.setDataSource(dataSource()); lsf.setPackagesToScan("your.package");
Properties props = new Properties();
props.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect"); props.put("hibernate.hbm2ddl.auto", "update"); lsf.setHibernateProperties(props);
return lsf;
}

@Bean
public HibernateTransactionManager transactionManager(SessionFactory sf) { return new HibernateTransactionManager(sf);
}

@Bean
public BankService bankService(SessionFactory sf) { return new BankService(sf);
}
}
public class MainApp {
public static void main(String[] args) { AnnotationConfigApplicationContext ctx = new
AnnotationConfigApplicationContext(AppConfig.class);
BankService service = ctx.getBean(BankService.class);

try {
service.transferMoney(101, 102, 500); System.out.println("Transaction Successful!");
} catch (Exception e) {
System.out.println("Transaction Failed: " + e.getMessage());
}
ctx.close();
}
}

