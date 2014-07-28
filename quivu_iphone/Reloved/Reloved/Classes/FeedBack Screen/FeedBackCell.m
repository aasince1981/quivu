//
//  FeedBackCell.m
//  quivu
//
//  Created by Kamal on 04/06/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "FeedBackCell.h"

@implementation FeedBackCell
@synthesize btnFeedBackImage,imgIcon,imgUserImage,lblMessage,lblFeedBackTime,lblUserName,delegate,getIndex,lblFeedBackReplyMessage,lblFeedBackReplyTime,lblReplyUserName,btnEditIconImage,btnArrowRight,imgClockFeedBackTime,imgClockReplyTime;

- (void)awakeFromNib
{
    lblFeedBackTime.font = FONT_Lato_Bold(13.0f);
    lblMessage.font = FONT_Lato_Bold(13.0f);
    lblUserName.font = FONT_Lato_Bold(12.0f);
    
    lblReplyUserName.font = FONT_Lato_Bold(12.0f);
    lblFeedBackReplyTime.font = FONT_Lato_Bold(13.0f);
    lblFeedBackReplyMessage.font = FONT_Lato_Bold(13.0f);
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (IBAction)BtnFeedBackReply:(id)sender {
    [delegate FeedBackReplyButtonArrow:getIndex];
}

- (IBAction)btnEditIconImage:(id)sender {
    [delegate FeedBackEditIconButton:getIndex];
}

@end
