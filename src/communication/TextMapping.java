package communication;

import communication.ObservableGame.MENU_ITEM;

public class TextMapping {

	public static final int TXT_YOU_LOSE = 1;
	public static final int TXT_YOU_WIN = 2;
	public static final int TXT_MAIN_MENU = 3;
	public static final int TXT_NEW_GAME = 4;
	public static final int TXT_CONTINUE = 5;
	public static final int TXT_LEVEL_CHOOSE = 6;
	public static final int TXT_END = 7;
	public static final int TXT_GAME_PAUSED = 8;
	
	/**
	 * This mthod returns a String for Txt index
	 * @param indexOfTxt
	 * @return
	 */
	public static String getTextForIndex(int indexOfTxt) {
		switch (indexOfTxt) {
		case TXT_YOU_LOSE:
			return "Sie haben verloren!";
		case TXT_YOU_WIN:
			return "Sie haben gewonnen!";
		case TXT_MAIN_MENU:
			return "Hauptmenü";
		case TXT_NEW_GAME:
			return "Neues Spiel";
		case TXT_CONTINUE:
			return "Weiterspielen";
		case TXT_LEVEL_CHOOSE:
			return "Level wählen";
		case TXT_END:
			return "Spiel beenden";
		case TXT_GAME_PAUSED:
			return "Spiel angehalten";
		default:
			return "?";			
		}
	}
	
	public static String getTextForMenuEnum(MENU_ITEM mnuEnum) {
		switch (mnuEnum) {
		case MNU_NEW_GAME:
			return getTextForIndex(TXT_NEW_GAME);
		case MNU_END:
			return getTextForIndex(TXT_END);
		case MNU_CONTINUE:
			return getTextForIndex(TXT_CONTINUE);
		case MNU_LEVEL_CHOOSE:
			return getTextForIndex(TXT_LEVEL_CHOOSE);
		default:
			return "?";
		}
	}
	
}
