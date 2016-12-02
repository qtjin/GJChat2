package com.gj.gjchat2.util;


import com.hyphenate.easeui.domain.EaseUser;

import java.util.Comparator;

/**
 * 
 * @author 
 *
 */
public class PinyinComparator implements Comparator<EaseUser> {

	@Override
	public int compare(EaseUser o1, EaseUser o2) {
		if (o1.getInitialLetter().equals("@")
				|| o2.getInitialLetter().equals("#")) {
			return -1;
		} else if (o1.getInitialLetter().equals("#")
				|| o2.getInitialLetter().equals("@")) {
			return 1;
		} else {
			return o1.getInitialLetter().compareTo(o2.getInitialLetter());
		}
	}
}
