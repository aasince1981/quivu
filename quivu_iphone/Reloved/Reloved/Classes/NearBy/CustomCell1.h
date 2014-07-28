//
//  CustomCell1.h
//  Rideout
//
//  Created by shiv sharma on 19/11/13.
//  Copyright (c) 2013 Ideal It Technology. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CustomCell1 : UITableViewCell
{
    
}

+(NSString*) reuseIdentifier;


@property (strong, nonatomic) IBOutlet UILabel *name;

@property (strong, nonatomic) NSIndexPath *indexPath;
@property (strong, nonatomic) IBOutlet UIImageView *Image_view;

@end
