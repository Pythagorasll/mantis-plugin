package hudson.plugins.mantis.changeset;

import hudson.model.AbstractBuild;
import hudson.model.User;
import hudson.scm.ChangeLogSet.Entry;
import hudson.scm.RepositoryBrowser;
import hudson.scm.SCM;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.logging.Logger;

/**
 * AbstractChangeSet
 * @author Seiji Sogabe
 * @since 0.7
 */
public abstract class AbstractChangeSet<T extends Entry> implements ChangeSet, Serializable {

    protected int id;
    protected AbstractBuild<?, ?> build;
    protected T entry;
    protected String gitweb_path;
    protected String noOfChangedFiles;

    public AbstractChangeSet(final int id, final AbstractBuild<?, ?> build, final T entry) {
        this.id = id;
        this.build = build;
        this.entry = entry;
        this.gitweb_path = "";
        this.noOfChangedFiles = "";
    }

    public int getId() {
        return id;
    }

    public abstract String createChangeLog();

    protected RepositoryBrowser getRepositoryBrowser() {
        if (build == null || build.getProject() == null) {
            return null;
        }
        final SCM scm = build.getProject().getScm();
        return scm.getBrowser();
    }

    protected String getChangeSetLink() {
        @SuppressWarnings("unchecked")
        final RepositoryBrowser<T> browser = getRepositoryBrowser();
        if (browser == null) {
            return UNKNOWN_CHANGESETLINK;
        }

        String link = UNKNOWN_CHANGESETLINK;
        try {
            @SuppressWarnings("unchecked")
            final URL url = browser.getChangeSetLink(entry);
            if (url != null) {
                link = url.toString();
            }
        } catch (final IOException e) {
            LOGGER.warning(e.getMessage());
        }
        return link;
    }

    protected String getAuthor() {
        final User user = entry.getAuthor();
        if (user == null) {
            return UNKNOWN_AUTHOR;
        }
        //GetID returns 'firstname.lastname'
        //Instead of 'Firstname Lastname'
        //We might be able to use user.getAuthorName() instead eventhough
        //comments on getAuthorName says
        /* Gets the author name for this changeset - note that this is mainly here
        * so that we can test authorOrCommitter without needing a fully instantiated
        * Hudson (which is needed for User.get in getAuthor()).
        * @return author name
        * https://github.com/jenkinsci/git-plugin/blob/master/src/main/java/hudson/plugins/git/GitChangeSet.java
        */
        //We might be able to use user.getDisplayName() or user.getFullName() as defined here
        //http://javadoc.jenkins-ci.org/hudson/model/User.html

        return user.getId();
    }

    protected String getMsg() {
        if (entry == null) {
            return UNKNOWN_MSG;
        }
        return entry.getMsg();
    }

    public void addGitwebPath(String gitwebPath) {
    	this.gitweb_path = gitwebPath;
    }

    public void addNoOfChangedFiles(String noOfChangedFiles) {
    	this.noOfChangedFiles = noOfChangedFiles;
    }

    private static final Logger LOGGER = Logger.getLogger(AbstractChangeSet.class.getName());
}
