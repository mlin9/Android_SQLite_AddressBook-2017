package michaellin.lab_assignment_9;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Jimmy on 11/5/2017.
 */

public class StudentContentProvider extends ContentProvider {

    String AUTHORITY = "students";
    String PATH_STUDENT_LIST = "STUDENT_LIST";
    String PATH_STUDENT_ADDRESS = "STUDENT_ADDRESS";
    String PATH_STUDENT_COUNT = "STUDENT_COUNT";

    Uri URI_1 = Uri.parse("content://" + AUTHORITY + "/" + PATH_STUDENT_LIST);
    Uri URI_2 = Uri.parse("content://" + AUTHORITY + "/" + PATH_STUDENT_ADDRESS);
    Uri URI_3 = Uri.parse("content://" + AUTHORITY + "/" + PATH_STUDENT_COUNT);

    final int STUDENT_LIST = 1;
    final int STUDENT_ADDRESS = 2;
    final int STUDENT_COUNT = 3;

    UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    {
        MATCHER.addURI(AUTHORITY, PATH_STUDENT_LIST, STUDENT_LIST);
        MATCHER.addURI(AUTHORITY, PATH_STUDENT_ADDRESS, STUDENT_ADDRESS);
        MATCHER.addURI(AUTHORITY, PATH_STUDENT_COUNT, STUDENT_COUNT);
    }

    String MIME_1 = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "students.id";
    String MIME_2 = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "students.name";
    String MIME_3 = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "students.grade";

    DatabaseAdapter database_adapter;

    @Override
    public boolean onCreate()
    {
        database_adapter = DatabaseAdapter.getAdapterInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,@Nullable String[] projection,@Nullable String selection,@Nullable String[] selectionArgs,@Nullable String sortOrder)
    {
        Cursor cursor = null;
        switch(MATCHER.match(uri))
        {
            case STUDENT_LIST: cursor = database_adapter.getAllCursors(); break;
            case STUDENT_ADDRESS: cursor = database_adapter.getCursor(selectionArgs[0]); break;
            case STUDENT_COUNT: cursor = database_adapter.getCount(); break;
            default: cursor = null; break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri,@Nullable ContentValues contentValues) throws UnsupportedOperationException
    {
        Uri return_uri = null;
        switch(MATCHER.match(uri))
        {
            case STUDENT_LIST: return_uri = insertStudent(uri, contentValues); break;
            default: new UnsupportedOperationException("Insert Operation Not Supported."); break;
        }

        return return_uri;
    }

    @Override
    public int update(@NonNull Uri uri,@Nullable ContentValues contentValues,@Nullable String s,@Nullable String[] strings) throws UnsupportedOperationException
    {
        int update_count = -1;
        switch (MATCHER.match(uri))
        {
            case STUDENT_LIST: update_count = update(contentValues, s, strings); break;
            default: new UnsupportedOperationException("Insert Operation Not Supported");
        }
        return update_count;
    }

    private int update(ContentValues contentValues, String clause, String[] strings)
    {
        return database_adapter.update(contentValues, clause, strings);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri)
    {
        switch(MATCHER.match(uri))
        {
            case STUDENT_LIST: return MIME_1;
            case STUDENT_ADDRESS: return MIME_2;
            case STUDENT_COUNT: return MIME_3;
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) throws UnsupportedOperationException
    {
        int delete_count = -1;
        switch(MATCHER.match(uri))
        {
            case STUDENT_LIST: delete_count = delete(s, strings); break;
            default: new UnsupportedOperationException("Delete Operation Not Supported"); break;
        }
        return delete_count;
    }

    private int delete(String clause, String[] values)
    {
        return database_adapter.delete(clause, values);
    }

    private Uri insertStudent(Uri uri, ContentValues contentValues)
    {
        long id = database_adapter.insert(contentValues);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse("content://" + AUTHORITY + "/" + PATH_STUDENT_LIST + "/" + id);
    }
}