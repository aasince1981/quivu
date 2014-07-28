//
//  Image.m
//  Rideout
//
//  Created by Ideal IT Techno on 07/03/14.
//  Copyright (c) 2014 Ideal It Technology. All rights reserved.
//

#import "Image.h"

@implementation Image

/*- (BOOL) InternetStatus
{
    if ([KAppDelegate isInternetAvailable]) {
        return YES;
    } else {
        [[[UIAlertView alloc] initWithTitle:@"" message:kInternetNotAvailable delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil] show];
        return NO;
    }
}*/

- (BOOL) isObject:(id) ob String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:(int) which
{
    @try {
        if (ob) {
            if (which == 1) {
                if ([ob isKindOfClass:[NSString class]])
                    return YES;
                else
                    return NO;
            } else if (which == 2) {
                if ([ob isKindOfClass:[NSArray class]])
                    return YES;
                else
                    return NO;
            } else if (which == 3) {
                if ([ob isKindOfClass:[NSDictionary class]])
                    return YES;
                else
                    return NO;
            } else if (which == 4) {
                if ([ob intValue] != 0)
                    return YES;
                else
                    return NO;
            } else if (which == 5) {
                if ([ob isKindOfClass:[UIImage class]])
                    return YES;
                else
                    return NO;
            }
        } else {
            return NO;
        }
        
        return NO;
    } @catch (NSException *exception) {
        return NO;
    }
}

- (BOOL) isObject:(id) view KindOf_UILabel_1_UIImageView_2_UIButton_3_View_4:(int) which
{
    @try {
        if (view) {
            if (which == 1) {
                if ([view isKindOfClass:[UILabel class]])
                    return YES;
                else
                    return NO;
            } else if (which == 2) {
                if ([view isKindOfClass:[UIImageView class]])
                    return YES;
                else
                    return NO;
            } else if (which == 3) {
                if ([view isKindOfClass:[UIButton class]])
                    return YES;
                else
                    return NO;
            } else if (which == 4) {
                if (view)
                    return YES;
            }
        } else {
            return NO;
        }
        
        return NO;
    } @catch (NSException *exception) {
        return NO;
    }
}

- (void) SetImageToView:(id) view fromImageUrl:(NSString *) url DefaultImage:(NSString *) defaultImage WithSizeToView:(BOOL) boolViewSize WithSizeToImage:(BOOL) boolImageSize WithSave:(BOOL) boolSave InBackground:(BOOL) boolInBackground
{
    @try {
        if ([self isObject:view KindOf_UILabel_1_UIImageView_2_UIButton_3_View_4:4]) {
            if ([self isObject:url String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
                if ([self isObject:defaultImage String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
                    NSArray * arr = [NSArray arrayWithObjects:view, url, defaultImage, [NSNumber numberWithBool:boolViewSize], [NSNumber numberWithBool:boolImageSize], [NSNumber numberWithBool:boolSave], [NSNumber numberWithBool:boolInBackground], nil];
                    [self performSelectorInBackground:@selector(SetImageToViewThread:) withObject:arr];
                }
            }
        }
    } @catch (NSException *exception) { }
}

- (void) SetImageToViewThread:(NSArray *) arr
{
    @try {
        id view = [arr objectAtIndex:0];
        NSString * url = [arr objectAtIndex:1];
        NSString * defaultImage = [arr objectAtIndex:2];
        BOOL boolViewSize = [[arr objectAtIndex:3] boolValue];
        BOOL boolImageSize = [[arr objectAtIndex:4] boolValue];
        BOOL boolSave = [[arr objectAtIndex:5] boolValue];
        BOOL boolInBackground = [[arr objectAtIndex:6] boolValue];
        
        @try {
            NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
            UIImage * image = [UIImage imageWithData:[defaults objectForKey:url]];
            
            if (image == nil) {
                NSURLRequest * request = [NSURLRequest requestWithURL:[NSURL URLWithString:url]];
                [NSURLConnection sendAsynchronousRequest:request
                                                   queue:[NSOperationQueue mainQueue]
                                       completionHandler:^(NSURLResponse * response, NSData * data, NSError * error)
                 {
                     UIImage * image;
                     if (data != nil) {
                         image = [UIImage imageWithData:data];
                         
                         if (boolSave) {
                             [defaults setValue:data forKey:url];
                             [defaults synchronize];
                         }
                     } else {
                         image = [UIImage imageNamed:defaultImage];
                     }
                     
                     if ([view isKindOfClass:[UIButton class]]) {
                         UIButton * btn = (UIButton *) view;
                         
                         if (boolInBackground) {
                             [btn setBackgroundImage:image forState:UIControlStateNormal];
                         } else {
                             [btn setImage:image forState:UIControlStateNormal];
                         }
                     } else if ([view isKindOfClass:[UIImageView class]]) {
                         UIImageView * img = (UIImageView *) view;
                         [img setImage:image];
                     }
                     
                     if (boolViewSize) {
                         [self SizeTheView:view FromImage:image];
                     } else if (boolImageSize) {
                         [self SizeTheImage:view AndUIImage:image];
                     }
                 }];
            } else {
                if ([view isKindOfClass:[UIButton class]]) {
                    UIButton * btn = (UIButton *) view;
                    if (boolInBackground) {
                        [btn setBackgroundImage:image forState:UIControlStateNormal];
                    } else {
                        [btn setImage:image forState:UIControlStateNormal];
                    }
                } else if ([view isKindOfClass:[UIImageView class]]) {
                    UIImageView * img = (UIImageView *) view;
                    [img setImage:image];
                }
                
                if (boolViewSize) {
                    [self SizeTheView:view FromImage:image];
                } else if (boolImageSize) {
                    [self SizeTheImage:view AndUIImage:image];
                }
            }
        } @catch (NSException *exception) {
            @try {
                UIImage * image = [UIImage imageNamed:defaultImage];
                if ([view isKindOfClass:[UIButton class]]) {
                    UIButton * btn = (UIButton *) view;
                    if (boolInBackground) {
                        [btn setBackgroundImage:image forState:UIControlStateNormal];
                    } else {
                        [btn setImage:image forState:UIControlStateNormal];
                    }
                } else if ([view isKindOfClass:[UIImageView class]]) {
                    UIImageView * img = (UIImageView *) view;
                    [img setImage:image];
                }
                
                if (boolViewSize) {
                    [self SizeTheView:view FromImage:image];
                } else if (boolImageSize) {
                    [self SizeTheImage:view AndUIImage:image];
                }
            } @catch (NSException *exception) { }
        }
    } @catch (NSException *exception) { }
}

- (void) SizeTheView:(id) view FromImage:(UIImage *) image
{
    @try {
        if ([self isObject:view KindOf_UILabel_1_UIImageView_2_UIButton_3_View_4:4]) {
            if ([self isObject:image String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:5]) {
                NSArray * arr = [NSArray arrayWithObjects:view, image, nil];
                [self performSelectorInBackground:@selector(SizeTheView:) withObject:arr];
            }
        }
    } @catch (NSException *exception) { }
}

- (void) SizeTheView:(NSArray *) arr
{
    sleep(1);
    @try {
        id view = [arr objectAtIndex:0];
        UIImage * image = [arr objectAtIndex:1];
        
        if ([view isKindOfClass:[UIButton class]]) {
            UIButton * button = (UIButton *) view;
            
            [button setImage:image forState:UIControlStateNormal];
            
            CGSize imgSize = image.size;
            
            float ratio=button.frame.size.width/imgSize.width;
            float scaledHeight=imgSize.height*ratio;
            
            float diff = button.frame.size.height - scaledHeight;
            if(scaledHeight < button.frame.size.height) {
                button.frame = CGRectMake(button.frame.origin.x, (diff/2)+button.frame.origin.y, button.frame.size.width, scaledHeight);
            }
            
            float ratioW=button.frame.size.height/imgSize.height;
            float scaledw=imgSize.width*ratioW;
            float diffW = button.frame.size.width - scaledw;
            if(scaledw < button.frame.size.width) {
                button.frame = CGRectMake((diffW/2)+button.frame.origin.x, button.frame.origin.y, scaledw, button.frame.size.height);
            }
        } else {
            UIImageView * button = (UIImageView *) view;
            [button setImage:image];
            
            CGSize imgSize = image.size;
            
            float ratio=button.frame.size.width/imgSize.width;
            float scaledHeight=imgSize.height*ratio;
            
            float diff = button.frame.size.height - scaledHeight;
            if(scaledHeight < button.frame.size.height) {
                button.frame = CGRectMake(button.frame.origin.x, (diff/2)+button.frame.origin.y, button.frame.size.width, scaledHeight);
            }
            
            float ratioW=button.frame.size.height/imgSize.height;
            float scaledw=imgSize.width*ratioW;
            float diffW = button.frame.size.width - scaledw;
            if(scaledw < button.frame.size.width) {
                button.frame = CGRectMake((diffW/2)+button.frame.origin.x, button.frame.origin.y, scaledw, button.frame.size.height);
            }
        }
    } @catch (NSException *exception) { }
}

- (void) SizeTheImage:(id) view AndUIImage:(UIImage *) image
{
    CGSize sizeView;
    CGSize sizeImage = image.size;
    
    if ([view isKindOfClass:[UIButton class]]) {
        UIButton * btn = (UIButton *) view;
        sizeView = btn.frame.size;
    } else {
        UIImageView * img = (UIImageView *) view;
        sizeView = img.frame.size;
    }
    
    double Wv = sizeView.width;
    double Hv = sizeView.height;

    double Wi = sizeImage.width;
    double Hi = sizeImage.height;

    int w = 1;
    int h = 2;
    
    int Vn = (Wv>Hv)?h:w;

    if (Vn == h) {
        if (Hv < Hi) {
            float Hp = (Hv/Hi)*100;
            float Nw = (Wi*Hp)/100;
            image = [self getImageWithSize:image withWidth:Nw andWithHeight:Hv];
        }
    } else {
        if (Wv < Wi) {
            float Wp = (Wv/Wi)*100;
            float Nh = (Hi*Wp)/100;
            image = image = [self getImageWithSize:image withWidth:Wv andWithHeight:Nh];
        }
    }
    
    if ([view isKindOfClass:[UIButton class]]) {
        UIButton * btn = (UIButton *) view;
        if (0) {
            [btn setBackgroundImage:image forState:UIControlStateNormal];
        } else {
            [btn setImage:image forState:UIControlStateNormal];
        }
    } else {
        UIImageView * img = (UIImageView *) view;
        [img setImage:image];
    }
}

- (UIImage *) getImageWithSize:(UIImage *) image withWidth:(float) width andWithHeight:(float) height
{
    UIImage *tempImage = nil;
    CGSize targetSize = CGSizeMake(width, height);
    UIGraphicsBeginImageContext(targetSize);
    
    CGRect thumbnailRect = CGRectMake(0, 0, 0, 0);
    thumbnailRect.origin = CGPointMake(0.0,0.0);
    thumbnailRect.size.width  = targetSize.width;
    thumbnailRect.size.height = targetSize.height;
    
    [image drawInRect:thumbnailRect];
    
    tempImage = UIGraphicsGetImageFromCurrentImageContext();
    
    UIGraphicsEndImageContext();
    
    return tempImage;
}

@end
