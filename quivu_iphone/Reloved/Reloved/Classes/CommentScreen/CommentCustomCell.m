//
//  CommentCustomCell.m
//  Reloved
//
//  Created by Kamal on 27/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "CommentCustomCell.h"

@implementation CommentCustomCell
@synthesize lblCommentMessage,lblDuration,lblUserName,imgUserImage,lblReplyUserName;

- (void)awakeFromNib
{
    // Initialization code
    
    lblCommentMessage.font = FONT_Lato_Bold(12.0f);
    lblDuration.font = FONT_Lato_Bold(11.0f);
    lblUserName.font = FONT_Lato_Bold(14.0f);
    lblReplyUserName.font = FONT_Lato_Bold(12.0f);
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
