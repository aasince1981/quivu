//
//  ActivityCell.m
//  Reloved
//
//  Created by GOURAV JOSHIE on 29/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "ActivityCell.h"

@implementation ActivityCell
@synthesize lbl_LastLogin,lbl_UserName_Msg,imgView_ProductImage,imgView_UserImage,imgIcon,btnUserName,delegate,getIndex,imgClockIcon;

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
        lbl_LastLogin.font = FONT_Lato_Bold(12.0f);
        btnUserName.titleLabel.font = FONT_Lato_Bold(12.0f);
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (IBAction)btnUserName:(id)sender {
    [delegate btnUserNameClick:getIndex];
}

@end
