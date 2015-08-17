package mobi.infolife.limitNumAndAir.XKUtils;

import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

/**
 * 姹夊瓧杞嫾闊崇被
 *
 * @author stuxuhai (dczxxuhai@gmail.com)
 * @version 1.0
 */
public class PinyinHelper {
	private static final Properties PINYIN_TABLE = PinyinResource
			.getPinyinTable();
	private static final Properties MUTIL_PINYIN_TABLE = PinyinResource
			.getMutilPintinTable();
	private static final String PINYIN_SEPARATOR = ",";
	private static final String ALL_UNMARKED_VOWEL = "aeiouv";
	private static final String ALL_MARKED_VOWEL = "āáǎàēéěèīíǐìōóǒòūúǔùǖǘǚǜ";

	private static String[] convertWithToneNumber(String pinyinArrayString) {
		String[] pinyinArray = pinyinArrayString.split(PINYIN_SEPARATOR);
		for (int i = pinyinArray.length - 1; i >= 0; i--) {
			boolean hasMarkedChar = false;
			String originalPinyin = pinyinArray[i].replaceAll("ü", "v");

			for (int j = originalPinyin.length() - 1; j >= 0; j--) {
				char originalChar = originalPinyin.charAt(j);

				if (originalChar < 'a' || originalChar > 'z') {
					int indexInAllMarked = ALL_MARKED_VOWEL
							.indexOf(originalChar);
					int toneNumber = indexInAllMarked % 4 + 1;
					char replaceChar = ALL_UNMARKED_VOWEL
							.charAt(((indexInAllMarked - indexInAllMarked % 4)) / 4);
					pinyinArray[i] = originalPinyin.replaceAll(
							String.valueOf(originalChar),
							String.valueOf(replaceChar))
							+ toneNumber;
					hasMarkedChar = true;
					break;
				}
			}
			if (!hasMarkedChar) {

				pinyinArray[i] = originalPinyin + "5";
			}
		}

		return pinyinArray;
	}

	private static String[] convertWithoutTone(String pinyinArrayString) {
		String[] pinyinArray;
		for (int i = ALL_MARKED_VOWEL.length() - 1; i >= 0; i--) {
			char originalChar = ALL_MARKED_VOWEL.charAt(i);
			char replaceChar = ALL_UNMARKED_VOWEL.charAt(((i - i % 4)) / 4);
			pinyinArrayString = pinyinArrayString.replaceAll(
					String.valueOf(originalChar), String.valueOf(replaceChar));
		}
		pinyinArray = pinyinArrayString.replaceAll("ü", "v").split(
				PINYIN_SEPARATOR);
		Set<String> pinyinSet = new LinkedHashSet<String>();
		for (String pinyin : pinyinArray) {
			pinyinSet.add(pinyin);
		}

		return pinyinSet.toArray(new String[pinyinSet.size()]);
	}

	private static String[] formatPinyin(String pinyinString,
			PinyinFormat pinyinFormat) {
		if (pinyinFormat == PinyinFormat.WITH_TONE_MARK) {
			return pinyinString.split(PINYIN_SEPARATOR);
		} else if (pinyinFormat == PinyinFormat.WITH_TONE_NUMBER) {
			return convertWithToneNumber(pinyinString);
		} else if (pinyinFormat == PinyinFormat.WITHOUT_TONE) {
			return convertWithoutTone(pinyinString);
		}
		return null;
	}

	public static String[] convertToPinyinArray(char c,
			PinyinFormat pinyinFormat) {
		String pinyin = PINYIN_TABLE.getProperty(String.valueOf(c));
		if ((pinyin != null) && (!pinyin.equals("null"))) {
			return formatPinyin(pinyin, pinyinFormat);
		}
		return null;
	}

	public static String[] convertToPinyinArray(char c) {
		return convertToPinyinArray(c, PinyinFormat.WITH_TONE_MARK);
	}

	public static String convertToPinyinString(String str, String separator,
			PinyinFormat pinyinFormat) {
		str = ChineseHelper.convertToSimplifiedChinese(str);
		StringBuilder sb = new StringBuilder();
		for (int i = 0, len = str.length(); i < len; i++) {
			char c = str.charAt(i);
			if (ChineseHelper.isChinese(c) || c == '〇') {
				boolean isFoundFlag = false;
				int rightMove = 3;
				for (int rightIndex = (i + rightMove) < len ? (i + rightMove)
						: (len - 1); rightIndex > i; rightIndex--) {
					String cizu = str.substring(i, rightIndex + 1);
					if (MUTIL_PINYIN_TABLE.containsKey(cizu)) {
						String[] pinyinArray = formatPinyin(
								MUTIL_PINYIN_TABLE.getProperty(cizu),
								pinyinFormat);
						for (int j = 0, l = pinyinArray.length; j < l; j++) {
							sb.append(pinyinArray[j]);
							if (j < l - 1) {
								sb.append(separator);
							}
						}
						i = rightIndex;
						isFoundFlag = true;
						break;
					}
				}
				if (!isFoundFlag) {
					String[] pinyinArray = convertToPinyinArray(str.charAt(i),
							pinyinFormat);
					if (pinyinArray != null) {
						sb.append(pinyinArray[0]);
					} else {
						sb.append(str.charAt(i));
					}
				}
				if (i < len - 1) {
					sb.append(separator);
				}
			} else {
				sb.append(c);
				if ((i + 1) < len && ChineseHelper.isChinese(str.charAt(i + 1))) {
					sb.append(separator);
				}
			}

		}
		return sb.toString();
	}

	public static String convertToPinyinString(String str, String separator) {
		return convertToPinyinString(str, separator,
				PinyinFormat.WITH_TONE_MARK);
	}

	public static boolean hasMultiPinyin(char c) {
		String[] pinyinArray = convertToPinyinArray(c);
		if (pinyinArray != null && pinyinArray.length > 1) {
			return true;
		}
		return false;
	}

	public static String getShortPinyin(String str) {
		String separator = "#";
		StringBuilder sb = new StringBuilder();

		char[] charArray = new char[str.length()];
		for (int i = 0, len = str.length(); i < len; i++) {
			char c = str.charAt(i);

			if (!ChineseHelper.isChinese(c) && c != '〇') {
				charArray[i] = c;
			} else {
				int j = i + 1;
				sb.append(c);

				// 鎼滅储杩炵画鐨勬眽瀛楀瓧绗︿覆
				while (j < len
						&& (ChineseHelper.isChinese(str.charAt(j)) || str
								.charAt(j) == '〇')) {
					sb.append(str.charAt(j));
					j++;
				}
				String hanziPinyin = convertToPinyinString(sb.toString(),
						separator, PinyinFormat.WITHOUT_TONE);
				String[] pinyinArray = hanziPinyin.split(separator);
				for (String string : pinyinArray) {
					charArray[i] = string.charAt(0);
					i++;
				}
				i--;
				sb.delete(0, sb.toString().length());
				sb.trimToSize();
			}
		}
		return String.valueOf(charArray);
	}

}
