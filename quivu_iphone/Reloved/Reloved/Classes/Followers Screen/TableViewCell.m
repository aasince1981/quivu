//
//  TableViewCell.m
//  Reloved
//
//  Created by Kamal on 24/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "TableViewCell.h"

@implementation TableViewCell
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

- (IBAction)BtnFollowAction:(id)sender {
    [BtnFollow setTitle:@"Loading..." forState:UIControlStateNormal];
     [delegate FollowerAndUnFollower:getIndex];
}

@end
