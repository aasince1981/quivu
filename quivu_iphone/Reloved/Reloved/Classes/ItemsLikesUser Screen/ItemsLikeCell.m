//
//  ItemsLikeCell.m
//  Reloved
//
//  Created by Kamal on 29/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "ItemsLikeCell.h"

@implementation ItemsLikeCell
@synthesize BtnFollow,delegate,img_ProfileImage,lbl_UserName,getIndex;

- (void)awakeFromNib
{
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (IBAction)BtnFollowAction:(id)sender {
    [BtnFollow setTitle:@"Loading..." forState:UIControlStateNormal];
    [delegate FollowAndFollowingLikeUser:getIndex];
}

@end
