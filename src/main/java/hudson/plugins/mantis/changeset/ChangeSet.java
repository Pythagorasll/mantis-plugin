package hudson.plugins.mantis.changeset;

/**
 * ChangeSet interface.
 * @author Seiji Sogabe
 * @since 0.7
 */
public interface ChangeSet {

    String UNKNOWN_AUTHOR = "-";

    String UNKNOWN_MSG = "-";

    String UNKNOWN_REVISION = "-";

    String UNKNOWN_CHANGESETLINK = "";

    String CRLF = System.getProperty("line.separator");

    int getId();

    void addGitwebPath(String gitwebPath);
    void addNoOfChangedFiles(String noOfChangedFiles);
    void addBranch(String newBranch);
    String createChangeLog();
}
