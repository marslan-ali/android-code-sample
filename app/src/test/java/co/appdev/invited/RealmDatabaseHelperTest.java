package co.appdev.invited;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import co.appdev.invited.data.local.DatabaseRealm;
import co.appdev.invited.utils.DefaultConfig;
import co.appdev.invited.utils.RxSchedulersOverrideRule;
import co.appdev.invited.utils.TestDataFactory;
import rx.observers.TestSubscriber;

import static junit.framework.Assert.assertEquals;

/**
 * Created by ahsan on 5/8/17.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = DefaultConfig.EMULATE_SDK)
public class RealmDatabaseHelperTest {


    //private final DatabaseRealm mDatabaseRealm = new DatabaseRealm(RuntimeEnvironment.application);
   @Mock
    DatabaseRealm mDatabaseRealm;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Test
    public void setUsers() {
        Users user1 = TestDataFactory.makeUser("r1");
        Users user2 = TestDataFactory.makeUser("r2");
        List<Users> users = Arrays.asList(user1, user2);

        TestSubscriber<Users> result = new TestSubscriber<>();
        mDatabaseRealm.setUsers(users).subscribe(result);
        result.assertNoErrors();
        result.assertReceivedOnNext(users);

        List<Users> usersList = (List<Users>) mDatabaseRealm.getUsers();
        assertEquals(2, usersList.size());
        for (Users user : usersList) {
            assertEquals(user,user);
        }
    }

    @Test
    public void getUser() {
        Users user1 = TestDataFactory.makeUser("r1");
        Users user2 = TestDataFactory.makeUser("r2");
        List<Users> users = Arrays.asList(user1, user2);

        mDatabaseRealm.setUsers(users).subscribe();

        TestSubscriber<List<Users>> result = new TestSubscriber<>();
        mDatabaseRealm.getUsers().subscribe(result);
        result.assertNoErrors();
        result.assertValue(users);
    }
}
