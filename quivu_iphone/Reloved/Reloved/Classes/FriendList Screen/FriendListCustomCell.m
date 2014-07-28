//
//  FriendListCustomCell.m
//  quivu
//
//  Created by Kamal on 12/06/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "FriendListCustomCell.h"

@implementation FriendListCustomCell
@synthesize btnFollow,lblName,img_ImageView,delegate,getIndex;

- (void)awakeFromNib
{
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (IBAction)btnFollow:(id)sender {
    
    [btnFollow setTitle:@"Loading..." forState:UIControlStateNormal];
     [delegate FollowAndUnFollowFriends:getIndex];
}

@end
