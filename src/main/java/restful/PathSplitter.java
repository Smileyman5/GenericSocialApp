package restful;

/**
 * Created by alex on 3/12/2017.
 */
public class PathSplitter
{
    public static String[] getPaths(String infoPath)
    {
        return infoPath.substring(1).split("/");
    }

    private PathSplitter()
    {
        throw new AssertionError("Private constructor cannot be called.");
    }
}
