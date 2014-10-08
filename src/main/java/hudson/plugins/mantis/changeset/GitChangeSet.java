package hudson.plugins.mantis.changeset;

import hudson.scm.ChangeLogSet.AffectedFile;
import hudson.model.AbstractBuild;
import java.util.Collection;

/**
 * ChangeSet of Git.
 *
 * @author Seiji Sogabe
 * @since 0.7.1
 */
public class GitChangeSet extends AbstractChangeSet<hudson.plugins.git.GitChangeSet> {

    private static final long serialVersionUID = 1L;

    public GitChangeSet(final int id, final AbstractBuild<?, ?> build,
            final hudson.plugins.git.GitChangeSet entry) {
        super(id, build, entry);
    }

    @Override
    public String createChangeLog() {
        final StringBuilder text = new StringBuilder();
        text.append(getBranch());
        text.append(CRLF);
        text.append("Commit: ");
        text.append(getGitwebPath());
        text.append(getRevision());
        text.append(CRLF);
        text.append(Messages.ChangeSet_Author(getAuthor()));
        text.append(CRLF);
        text.append(CRLF);
        text.append(entry.getComment());
        text.append(CRLF);
        text.append(Messages.ChangeSet_ChangedPaths_Header());
        text.append(CRLF);

	int i = 0;
	for(final AffectedFile file : entry.getAffectedFiles()) {
		i += 1;
		if(i > get_int_of_changed_files())
        {
    		text.append("  + ");
    		text.append(entry.getAffectedFiles().size() - get_int_of_changed_files());
    		text.append(" other files changed.");
    		break;
   		}

		text.append(
			Messages.ChangeSet_ChangedPaths_Path(
            			ChangeSetUtil.getEditTypeMark(file.getEditType()),
            			file.getPath()));
            	text.append(CRLF);
	}

        text.append(CRLF);

        return text.toString();
    }

    protected String getRevision() {
        return String.valueOf(entry.getId());
    }

    private Collection<String> getAffectedPaths() {
        return entry.getAffectedPaths();
    }

    protected String getGitwebPath() {
        if (gitweb_path == null) {
            return "";
        }
        return gitweb_path;
    }

    protected int get_int_of_changed_files() {
        if (noOfChangedFiles == null) {
            return 0;
        }

        int retval = 0;
        try {
            retval = Integer.parseInt(noOfChangedFiles);
        } catch(NumberFormatException nfe) {
            retval = 0;
        }
        return retval;
    }

   protected String getBranch(){
      if (branch == null) {
         return "";
      }
      return branch;
   }
}
