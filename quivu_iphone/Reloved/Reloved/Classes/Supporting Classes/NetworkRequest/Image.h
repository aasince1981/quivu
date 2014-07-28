//
//  Image.h
//  Rideout
//
//  Created by Ideal IT Techno on 07/03/14.
//  Copyright (c) 2014 Ideal It Technology. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Image : NSObject
{
    
}

- (BOOL) isObject:(id) ob String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:(int) which;
- (BOOL) isObject:(id) view KindOf_UILabel_1_UIImageView_2_UIButton_3_View_4:(int) which;

- (void) SetImageToView:(id) view fromImageUrl:(NSString *) url DefaultImage:(NSString *) defaultImage WithSizeToView:(BOOL) boolViewSize WithSizeToImage:(BOOL) boolImageSize WithSave:(BOOL) boolSave InBackground:(BOOL) boolInBackground;
- (void) SizeTheView:(id) view FromImage:(UIImage *) image;

- (BOOL) InternetStatus;

@end



