//
//  FollowingTabCustomCell.m
//  Reloved
//
//  Created by Kamal on 30/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "FollowingTabCustomCell.h"

@implementation FollowingTabCustomCell
@synthesize img_ProfileImage,lbl_UserName,delegate,getIndex,BtnFollow;

- (void)awakeFromNib
{
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (IBAction)BtnFollowingAction:(id)sender {
    [BtnFollow setTitle:@"Loading..." forState:UIControlStateNormal];
    [delegate FollowAndFollowingMethod:getIndex];
}

@end
