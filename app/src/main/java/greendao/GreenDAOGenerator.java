package greendao;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

/**
 * Created by wallase on 2017/4/28.
 */
public class GreenDAOGenerator {


    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.example.wallase.locall.green_dao");

        addUser(schema);

        new DaoGenerator().generateAll(schema, "./app/src/main/java");

    }

    private static void addUser(Schema schema) {
        Entity user = schema.addEntity("Member");
        user.addIdProperty();
        user.addStringProperty("account").notNull();
        user.addStringProperty("email");
        user.addStringProperty("device_token");
        user.addStringProperty("api_token");
    }
}
