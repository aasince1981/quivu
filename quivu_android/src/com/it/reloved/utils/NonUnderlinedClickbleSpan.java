package com.it.reloved.utils;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class NonUnderlinedClickbleSpan extends ClickableSpan 
{
	@Override
        public void updateDrawState(TextPaint ds) {
          // ds.setColor(ds.linkColor);
		    ds.setColor(Color.RED);
           ds.setUnderlineText(false); // set to false to remove underline
        }

	@Override
	public void onClick(View widget) {
		// TODO Auto-generated method stub
		
	}

}
