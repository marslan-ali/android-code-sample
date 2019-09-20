package co.appdev.invited;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import co.appdev.invited.data.DataManager;
import co.appdev.invited.data.local.DatabaseRealm;
import co.appdev.invited.data.local.PreferencesHelper;
import co.appdev.invited.data.remote.InvitedService;
import co.appdev.invited.utils.TestDataFactory;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This test class performs local unit tests without dependencies on the Android framework
 * For testing methods in the DataManager follow this approach:
 * 1. Stub mock helper classes that your method relies on. e.g. RetrofitServices or DatabaseHelper
 * 2. Test the Observable using TestSubscriber
 * 3. Optionally write a SEPARATE test that verifies that your method is calling the right helper
 * using Mockito.verify()
 */
@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {

    @Mock
    DatabaseRealm mMockDatabaseRealmHelper;
    @Mock
    PreferencesHelper mMockPreferencesHelper;
    @Mock
    InvitedService mMockRibotsService;
    private DataManager mDataManager;

    @Before
    public void setUp() {
        mDataManager = new DataManager(mMockRibotsService, mMockPreferencesHelper, mMockDatabaseRealmHelper);
    }


    @Test
    public void syncRibotsEmitsValues() {
        List<Users> users = Arrays.asList(TestDataFactory.makeUser("r1"),
                TestDataFactory.makeUser("r2"));
        stubSyncRibotsHelperCalls(users);

        TestSubscriber<Users> result = new TestSubscriber<>();
        mDataManager.syncUsers().subscribe(result);
        result.assertNoErrors();
        result.assertReceivedOnNext(users);
    }

    @Test
    public void syncRibotsCallsApiAndDatabase() {
        List<Users> users = Arrays.asList(TestDataFactory.makeUser("r1"),
                TestDataFactory.makeUser("r2"));
        stubSyncRibotsHelperCalls(users);

        mDataManager.syncUsers().subscribe();
        // Verify right calls to helper methods
        verify(mMockRibotsService).getUsers();
        verify(mMockDatabaseRealmHelper).setUsers(users);
    }



    @Test
    public void syncRibotsDoesNotCallDatabaseWhenApiFails() {
        when(mMockRibotsService.getUsers())
                .thenReturn(Observable.<List<Users>>error(new RuntimeException()));

        mDataManager.syncUsers().subscribe(new TestSubscriber<Users>());
        // Verify right calls to helper methods
        verify(mMockRibotsService).getUsers();
        verify(mMockDatabaseRealmHelper, never()).setUsers(ArgumentMatchers.<Users>anyList());
    }

    private void stubSyncRibotsHelperCalls(List<Users> users) {
        // Stub calls to the ribot service and database helper.
        when(mMockRibotsService.getUsers())
                .thenReturn(Observable.just(users));
        when(mMockDatabaseRealmHelper.setUsers(users))
                .thenReturn(Observable.from(users));
    }
}
