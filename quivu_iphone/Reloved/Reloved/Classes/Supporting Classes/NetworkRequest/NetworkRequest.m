//
//  Image.m
//  Rideout
//
//  Created by Ideal IT Techno on 07/03/14.
//  Copyright (c) 2014 Ideal It Technology. All rights reserved.
//

#import "NetworkRequest.h"
#import "LoadingViewController.h"
#import "AFHTTPClient.h"
#import "AFJSONRequestOperation.h"

@implementation NetworkRequest

- (void) HHH
{
    // GCD type 1
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_LOW, 0), ^{ // perform some kind of long-running task
        // maybe fetching from a network store or intense computation
        dispatch_async(dispatch_get_main_queue(), ^{ // perhaps update the user interface
        });
    });
    
    /// Thread
    [NSThread detachNewThreadSelector:@selector(HHH) toTarget:self withObject:nil];
}

- (id) JSONFromWebServices:(NSString *) webservice Parameter:(NSMutableDictionary *) param AI:(BOOL) boolAI PopUP:(BOOL) boolPopUP WithBlock:(void (^)(id json))block
{
    @try {
        if ([self InternetStatus:boolPopUP]) {
            webservice = [webservice stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
            
            AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:[NSURL URLWithString:webservice]];
            [httpClient setParameterEncoding:AFFormURLParameterEncoding];
            NSMutableURLRequest *request;
            
            request = [httpClient requestWithMethod:@"POST" path:webservice parameters:param];
            [AFJSONRequestOperation addAcceptableContentTypes:[NSSet setWithObject:@"text/html"]];
            
            AFJSONRequestOperation *operation = [AFJSONRequestOperation JSONRequestOperationWithRequest:request                                                                              success:^(NSURLRequest *request, NSHTTPURLResponse *response, id JSON) {
                block(JSON);
                
                if (boolAI)
                    [[LoadingViewController instance] stopRotation];
            } failure:^(NSURLRequest *request, NSHTTPURLResponse *response, NSError *error, id JSON) {
                NSLog(@"JSON = %@-", JSON);
                NSLog(@"error = %@-", error);
                block(JSON);
                
                if (boolPopUP)
                    dispatch_async(dispatch_get_main_queue(), ^{
                        [[[UIAlertView alloc] initWithTitle:@"" message:kCouldnotconnect delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil] show];
                    });
                
                if (boolAI)
                    [[LoadingViewController instance] stopRotation];
            }];
            
            [operation start];
            
            if (boolAI)
                [[LoadingViewController instance] startRotation];
        }
    } @catch (NSException *exception) {
    }
    
    return self;
}

-(BOOL)isInternetAvailable
{
    NetworkReachability * objReach = [NetworkReachability reachabilityForInternetConnection];
    NetworkStatus status = [objReach currentReachabilityStatus];
    
    if (status == NotReachable)
        return NO;
    
    return YES;
}

- (BOOL) InternetStatus:(BOOL) popup
{
    if ([self isInternetAvailable]) {
        return YES;
    } else if (popup) {
        dispatch_async(dispatch_get_main_queue(), ^{
            //[[[UIAlertView alloc] initWithTitle:@"" message:kInternetNotAvailable delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
        });
        
        return NO;
    }
    
    return YES;
}

- (BOOL) InternetStatus
{
    if ([self isInternetAvailable]) {
        return YES;
    } else {
        dispatch_async(dispatch_get_main_queue(), ^{
            [[[UIAlertView alloc] initWithTitle:@"" message:kInternetNotAvailable delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
        });
        
        return NO;
    }
    
    return YES;
}

- (BOOL) isObject:(id) ob String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:(int) which
{
    @try {
        if (ob) {
            if (which == 1) {
                if ([ob isKindOfClass:[NSString class]]) {
                    return YES;
                } else {
                    if ([ob isKindOfClass:[NSMutableString class]])
                        return YES;
                    else
                        return NO;
                }
            } else if (which == 2) {
                if ([ob isKindOfClass:[NSArray class]]) {
                    return YES;
                } else {
                    if ([ob isKindOfClass:[NSMutableArray class]])
                        return YES;
                    else
                        return NO;
                }
            } else if (which == 3) {
                if ([ob isKindOfClass:[NSDictionary class]]) {
                    return YES;
                } else {
                    if ([ob isKindOfClass:[NSMutableDictionary class]])
                        return YES;
                    else
                        return NO;
                }
            } else if (which == 4) {
                if ([ob isKindOfClass:[NSNumber class]]) {
                    return YES;
                } else {
                    return NO;
                }
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

- (void) SetImageToView:(id) view fromImageUrl:(NSString *) url Option:(int) option
{
    switch (option) {
            
        case IV_Save :
            [self SetImageToView:view fromImageUrl:url DefaultImage:@"" WithSizeToView:NO WithSizeToImage:NO WithSave:YES InBackground:NO];
            break;
        case IV_SizeView :
            [self SetImageToView:view fromImageUrl:url DefaultImage:@"" WithSizeToView:YES WithSizeToImage:NO WithSave:NO InBackground:NO];
            break;
        case IV_SizeImage :
            [self SetImageToView:view fromImageUrl:url DefaultImage:@"" WithSizeToView:NO WithSizeToImage:YES WithSave:NO InBackground:NO];
            break;
        case IV_SaveSizeView :
            [self SetImageToView:view fromImageUrl:url DefaultImage:@"" WithSizeToView:YES WithSizeToImage:NO WithSave:YES InBackground:NO];
            break;
        case IV_SaveSizeImage :
            [self SetImageToView:view fromImageUrl:url DefaultImage:@"" WithSizeToView:NO WithSizeToImage:YES WithSave:YES InBackground:NO];
            break;
        case IV_BackGroundSave :
            [self SetImageToView:view fromImageUrl:url DefaultImage:@"" WithSizeToView:NO WithSizeToImage:NO WithSave:YES InBackground:YES];
            break;
        case IV_BackGroundSizeView :
            [self SetImageToView:view fromImageUrl:url DefaultImage:@"" WithSizeToView:YES WithSizeToImage:NO WithSave:NO InBackground:YES];
            break;
        case IV_BackGroundSizeImage :
            [self SetImageToView:view fromImageUrl:url DefaultImage:@"" WithSizeToView:NO WithSizeToImage:YES WithSave:YES InBackground:YES];
            break;
        case IV_BackGroundSaveSizeView :
            [self SetImageToView:view fromImageUrl:url DefaultImage:@"" WithSizeToView:YES WithSizeToImage:NO WithSave:YES InBackground:YES];
            break;
        case IV_BackGroundSaveSizeImage :
            [self SetImageToView:view fromImageUrl:url DefaultImage:@"" WithSizeToView:NO WithSizeToImage:YES WithSave:YES InBackground:YES];
            break;
        case IV_BackGroundSaveSizeImageSizeView :
            [self SetImageToView:view fromImageUrl:url DefaultImage:@"" WithSizeToView:YES WithSizeToImage:YES WithSave:YES InBackground:YES];
            break;
        default:
            break;
    }
}

- (void) SetImageToView:(id) view fromImageUrl:(NSString *) url DefaultImage_SizeViewYES_1_SizeImageYES_2_SaveYES_3_BackYES_4_DefalutAllNO_5:(int) which
{
    if (which == 1) {
        [self SetImageToView:view fromImageUrl:url DefaultImage:@"" WithSizeToView:YES WithSizeToImage:NO WithSave:NO InBackground:NO];
    } else if (which == 2) {
        [self SetImageToView:view fromImageUrl:url DefaultImage:@"" WithSizeToView:NO WithSizeToImage:YES WithSave:NO InBackground:NO];
    } else if (which == 3) {
        [self SetImageToView:view fromImageUrl:url DefaultImage:@"" WithSizeToView:NO WithSizeToImage:NO WithSave:YES InBackground:NO];
    } else if (which == 4) {
        [self SetImageToView:view fromImageUrl:url DefaultImage:@"" WithSizeToView:NO WithSizeToImage:NO WithSave:NO InBackground:YES];
    } else {
        [self SetImageToView:view fromImageUrl:url DefaultImage:@"" WithSizeToView:NO WithSizeToImage:NO WithSave:NO InBackground:NO];
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
        
        url = [url stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        
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
                             [defaults setObject:data forKey:url];
                             [defaults synchronize];
                         }
                     } else {
                         image = [UIImage imageNamed:defaultImage];
                     }
                     
                     if ([self isObject:view KindOf_UILabel_1_UIImageView_2_UIButton_3_View_4:4]){
                         if ([self isObject:image String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:5]) {
                             [image ImageToView:view InBack:boolInBackground];
                         }
                     }
                     
                     if (boolViewSize) {
                         [self SizeTheView:view FromImage:image];
                     } else if (boolImageSize) {
                         [self SizeTheImage:view AndUIImage:image];
                     }
                 }];
            } else {
                if ([self isObject:view KindOf_UILabel_1_UIImageView_2_UIButton_3_View_4:4]){
                    if ([self isObject:image String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:5]) {
                        [image ImageToView:view InBack:boolInBackground];
                    }
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
                [image ImageToView:view InBack:boolInBackground];
                
                if (boolViewSize) {
                    [self SizeTheView:view FromImage:image];
                } else if (boolImageSize) {
                    [self SizeTheImage:view AndUIImage:image];
                }
            } @catch (NSException *exception) { }
        }
    } @catch (NSException *exception) { }
}

- (void) SetImageToView:(NSArray *) arr
{
    if (arr.count == 3) {
        id view = [arr objectAtIndex:0];
        UIImage * image = [arr objectAtIndex:1];
        BOOL boolInBackground = [[arr objectAtIndex:2] boolValue];
        
        [image ImageToView:view InBack:boolInBackground];
    }
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
    @try {
        if ([self isObject:view KindOf_UILabel_1_UIImageView_2_UIButton_3_View_4:4]) {
            if ([self isObject:image String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:5]) {
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
        }
    } @catch (NSException *exception) { }
}

- (UIImage *) getImageWithSize:(UIImage *) image withWidth:(float) width andWithHeight:(float) height
{
    @try {
        if ([self isObject:image String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:5]) {
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
    } @catch (NSException *exception) { }
}

- (void) PopUpMSG:(NSString *) msg Header:(NSString *) header
{
    if ([self isObject:msg String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
        if (![self isObject:header String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1])
            header = @"";
        [self performSelectorOnMainThread:@selector(PopUpMSGThread:) withObject:[NSArray arrayWithObjects:msg, header, nil] waitUntilDone:NO];
    }
}

- (void) PopUpMSGThread:(NSArray *) arr
{
    if ([self isObject:[arr objectAtIndex:0] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
        if (![self isObject:[arr objectAtIndex:1] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1])
            [[[UIAlertView alloc] initWithTitle:@"" message:[arr objectAtIndex:0] delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
        else
            [[[UIAlertView alloc] initWithTitle:[arr objectAtIndex:1] message:[arr objectAtIndex:0] delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
    }
}

- (void) setDefaultUserData:(NSMutableDictionary *) dict WithKey:(NSString *) key
{
    NSUserDefaults * defaults = [NSUserDefaults standardUserDefaults];
    
    NSData *myData = [NSKeyedArchiver archivedDataWithRootObject:dict];
    [defaults setObject:myData forKey:key];
    [defaults synchronize];
}

- (NSMutableDictionary *) getDefaultUserDataWithKey:(NSString *) key
{
    NSUserDefaults * defaults = [NSUserDefaults standardUserDefaults];
    
    NSMutableDictionary * dict = (NSMutableDictionary*) [NSKeyedUnarchiver unarchiveObjectWithData:[defaults objectForKey:key]];
    return dict;
}

- (void) GetDurationWithCurrentLat:(float) cLat CurrentLong:(float) cLong AndPickupLat:(float) pLat PickupLong:(float) pLong ToLabel:(UILabel *) lbl WithExtraText:(NSString *) extra DistToLabel:(UILabel *) lblDist
{
    NSString * url = [NSString stringWithFormat:@"http://maps.googleapis.com/maps/api/distancematrix/json?origins=%f,%f&destinations=%f,%f&mode=driving&sensor=false", cLat, cLong, pLat, pLong];
    
    //NSLog(@"setLocation-%@",url);
    [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:YES WithBlock:^(id json) {
        //NSLog(@"setLocation-->%@", dictJson);
        if (json != nil) {
            if ([[json objectForKey:@"status"] isEqualToString:@"OK"]) {
                NSArray * arrrows = [json objectForKey:@"rows"];
                if (arrrows.count > 0) {
                    //tfPickUp.text = [arrOrigin_addresses objectAtIndex:0];
                    //NSLog(@"strClick1-%@-", strClick1);
                    
                    NSDictionary * dd = [arrrows objectAtIndex:0];
                    NSArray * aaa = [dd objectForKey:@"elements"];
                    if (aaa.count > 0) {
                        NSDictionary * ddd = [aaa objectAtIndex:0];
                        if ([[ddd objectForKey:@"status"] isEqualToString:@"OK"]) {
                            NSDictionary * dur = [ddd objectForKey:@"duration"];
                            //[KAppDelegate setETA:[NSString stringWithFormat:@"%@ %@", extra, [dur objectForKey:@"text"]]];
                            
                            NSDictionary * dis = [ddd objectForKey:@"distance"];
                            
                            @try {
                                if (lbl)
                                    lbl.text = [NSString stringWithFormat:@"%@ %@", extra, [dur objectForKey:@"text"]];
                            } @catch (NSException *exception) { }
                            
                            @try {
                                if (lblDist)
                                    lblDist.text = [NSString stringWithFormat:@"Distance: %@", [dis objectForKey:@"text"]];
                            } @catch (NSException *exception) { }
                        }
                    }
                }
            }
        }
    }];
    
    
}

@end
