import org.apache.commons.io.IOUtils;
import org.junit.Test;
import parquet.resultset.ResultSetParquetTransformer;
import parquet.resultset.TransformerListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;

/**
 * Note, to run this on windows, you will need to to
 *
 * 1. Install Hadoop for windows
 *    Download from: https://github.com/karthikj1/Hadoop-2.7.1-Windows-64-binaries and unzip.
 *    Associated error message: "java.io.IOException: (null) entry in command string: null chmod"
 *
 * 2. Ensure that Microsoft Visual C++ 2010 SP1 Redistributable Package (x64) is installed.
 *    Download from: https://www.microsoft.com/en-us/download/confirmation.aspx?id=13523
 *    Associated error message: https://issues.apache.org/jira/browse/YARN-3524
 *
 * 3. Set the environment variable HADOOP_HOME to the location Hadoop was installed.
 *
 */
public class ResultSetParquetTransformerTest {

    @Test
    public void testToParquet() throws IOException, SQLException {

        String schemaName = "SchemaName";
        String namespace = "org.namespace";

        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);

        when(metaData.getColumnCount()).thenReturn(1);
        when(metaData.getColumnName(1)).thenReturn("id");
        when(metaData.getColumnType(1)).thenReturn(Types.INTEGER);


        List<TransformerListener> listeners = new ArrayList<>();

        ResultSetParquetTransformer transformer = new ResultSetParquetTransformer();
        InputStream inputStream = transformer.toParquet(resultSet, schemaName, namespace, listeners);


        IOUtils.copy(inputStream, new FileOutputStream(new File("test.par")));


    }

}