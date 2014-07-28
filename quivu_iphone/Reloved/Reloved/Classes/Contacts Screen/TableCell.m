//
//  TableCell.m
//  Digital Card
//
//  Created by ideal indore on 02/09/13.
//  Copyright (c) 2013 ideal indore. All rights reserved.
//

#import "TableCell.h"

@implementation TableCell
@synthesize lblName, lblNumber;

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
    [super setSelected:selected animated:NO];
    
    /*
    UIImage *selectionBackground = [[UIImage alloc] init];
    UIImageView *iview=[[UIImageView alloc] initWithImage:selectionBackground];
    self.selectedBackgroundView=iview;
    [self performSelectorInBackground:@selector(ResetHightlight) withObject:self];
     */
}

/*
- (void) ResetHightlight
{
    usleep(0500000);
    UIImage *selectionBackground = [[UIImage alloc] init];
    UIImageView *iview=[[UIImageView alloc] initWithImage:selectionBackground];
    self.selectedBackgroundView=iview;
}

- (void) showName_Numbers {
    [lblName setHidden:YES];
    [lblName setHidden:NO];
    [lblNumber setHidden:NO];
}

- (void) showOperations{
    [lblName setHidden:YES];
    [lblNumber setHidden:YES];
}
 */

@end
