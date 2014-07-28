//
//  CustomCell1.m
//  Rideout
//
//  Created by shiv sharma on 19/11/13.
//  Copyright (c) 2013 Ideal It Technology. All rights reserved.
//

#import "CustomCell1.h"

@implementation CustomCell1
@synthesize name,indexPath,Image_view;

+ (NSString *)reuseIdentifier {
    return @"CustomCellIdentifier";
}

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
