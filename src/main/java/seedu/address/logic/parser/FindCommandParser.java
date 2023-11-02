package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PENDING_QUESTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.predicates.AllPendingQuestionPredicate;
import seedu.address.model.person.predicates.NameContainsKeywordsPredicate;
import seedu.address.model.person.predicates.PQContainsKeywordsPredicate;
import seedu.address.model.person.predicates.RemarkContainsKeywordsPredicate;
import seedu.address.model.person.predicates.TagFilterPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * FindCommand
     * and returns a FindCommand object for execution.
     * 
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_TAG,
                PREFIX_PENDING_QUESTION, PREFIX_REMARK);
        
        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_TAG, PREFIX_PENDING_QUESTION, PREFIX_REMARK)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        // if (trimmedArgs.equals("pq")) {
        // return new FindCommand(new AllPendingQuestionPredicate());
        // }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_TAG, PREFIX_PENDING_QUESTION, PREFIX_REMARK);

        Optional<String> namePrefixValue = argMultimap.getValue(PREFIX_NAME);
        Optional<String> tagPrefixValue = argMultimap.getValue(PREFIX_TAG);
        Optional<String> pqPrefixValue = argMultimap.getValue(PREFIX_PENDING_QUESTION);
        Optional<String> remarkPrefixValue = argMultimap.getValue(PREFIX_REMARK);

        if (namePrefixValue.isPresent()) {
            return new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList(namePrefixValue.get())));
        } else if (tagPrefixValue.isPresent()) {
            return new FindCommand(new TagFilterPredicate(Arrays.asList(tagPrefixValue.get())));
        } else if (pqPrefixValue.isPresent()) {
            return new FindCommand(new PQContainsKeywordsPredicate(Arrays.asList(pqPrefixValue.get())));
        } else if (remarkPrefixValue.isPresent()) {
            return new FindCommand(new RemarkContainsKeywordsPredicate(Arrays.asList(remarkPrefixValue.get())));
        } else {
            // Handle the case where no prefix is present (this should not happen if you
            // check prefixes before calling parse)
            throw new ParseException("Invalid command format");
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values
     * in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        long count = Stream.of(prefixes)
                .filter(prefix -> argumentMultimap.getValue(prefix).isPresent())
                .count();
        return count == 1;
    }
}
