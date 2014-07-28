//
//  UIImage+SetImage.m
//  Deal Around
//
//  Created by Ideal IT Techno on 19/03/14.
//  Copyright (c) 2014 ideal indore. All rights reserved.
//

#import "UIImage+SetImage.h"

@implementation UIImage (ImageToview)

-(void) ImageToView:(id) view InBack:(BOOL) boolInBackground
{
    if ([view isKindOfClass:[UIButton class]]) {
        UIButton * btn = (UIButton *) view;
        if (boolInBackground) {
            [btn setBackgroundImage:self forState:UIControlStateNormal];
        } else {
            [btn setImage:self forState:UIControlStateNormal];
        }
    } else if ([view isKindOfClass:[UIImageView class]]) {
        UIImageView * img = (UIImageView *) view;
        [img setImage:self];
    }
}

@end
