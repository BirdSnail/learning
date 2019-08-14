package batch;

import lombok.ToString;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.JoinOperator;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.List;

/**
 * @author: Mr.Yang
 * @create: 2019-07-08
 */
public class CollectionExecutionExample {

	/**
	 * User POJO
	 */
	@ToString
	public static class User {
		public int userIdentifier;
		public String name;

		public User() {
		}

		public User(int userIdentifier, String name) {
			this.userIdentifier = userIdentifier;
			this.name = name;
		}

	}

	/**
	 * Email POJO
	 */
	@ToString
	public static class EMail {
		public int userId;
		public String subject;
		public String body;

		public EMail() {
		}

		public EMail(int userId, String subject, String body) {
			this.userId = userId;
			this.subject = subject;
			this.body = body;
		}
	}


	public static void main(String[] args) throws Exception {
		final ExecutionEnvironment env = ExecutionEnvironment.createCollectionsEnvironment();

		// create objects for users and emails
		User[] usersArray = {new User(1, "Peter"), new User(2, "John"),
				new User(3, "Bill")	,
				new User(1, "yang")};

		EMail[] emailsArray = {new EMail(1, "Re: Meeting", "How about 1pm?"),
				new EMail(1, "Re: Meeting", "Sorry, I'm not availble"),
				new EMail(3, "Re: Re: Project proposal", "Give me a few more days to think about it.")};

		// convert objects into a DataSet
		DataSet<User> users = env.fromElements(usersArray);
		DataSet<EMail> emails = env.fromElements(emailsArray);

		// join会把两个流放在一个tuple2里面
		DataSet<Tuple2<User, EMail>> joined = users.join(emails)
				.where("userIdentifier")
				.equalTo("userId");

		List<Tuple2<User, EMail>> result = joined.collect();
		result.stream().forEach(System.out::println);
	}

}
