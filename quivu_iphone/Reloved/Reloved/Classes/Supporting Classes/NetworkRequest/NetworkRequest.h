//
//  Image.h
//  Rideout
//
//  Created by Ideal IT Techno on 07/03/14.
//  Copyright (c) 2014 Ideal It Technology. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NetworkReachability.h"
#import "UIImage+SetImage.h"

#define obNet ((NetworkRequest*)[[NetworkRequest alloc] init])
#define isIPhone ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone)

#define IV_Save 1
#define IV_SizeView 2
#define IV_SizeImage 3
#define IV_SaveSizeView 4
#define IV_SaveSizeImage 5
#define IV_BackGroundSave 6
#define IV_BackGroundSizeView 7
#define IV_BackGroundSizeImage 8
#define IV_BackGroundSaveSizeView 9
#define IV_BackGroundSaveSizeImage 10
#define IV_BackGroundSaveSizeImageSizeView 11

@interface NetworkRequest : NSObject
{
    
}

- (BOOL) isObject:(id) ob String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:(int) which;
- (BOOL) isObject:(id) view KindOf_UILabel_1_UIImageView_2_UIButton_3_View_4:(int) which;

- (void) SetImageToView:(id) view fromImageUrl:(NSString *) url Option:(int) option;
/*
- (void) SetImageToView:(id) view fromImageUrl:(NSString *) url DefaultImage_SizeViewYES_1_SizeImageYES_2_SaveYES_3_BackYES_4_DefalutAllNO_5:(int) which;
- (void) SetImageToView:(id) view fromImageUrl:(NSString *) url DefaultImage:(NSString *) defaultImage WithSizeToView:(BOOL) boolViewSize WithSizeToImage:(BOOL) boolImageSize WithSave:(BOOL) boolSave InBackground:(BOOL) boolInBackground;
*/

- (void) SizeTheView:(id) view FromImage:(UIImage *) image;
- (void) SizeTheImage:(id) view AndUIImage:(UIImage *) image;

- (id) JSONFromWebServices:(NSString *) webservice
                 Parameter:(NSMutableDictionary *) param
                        AI:(BOOL) boolAI
                     PopUP:(BOOL) boolPopUP
                 WithBlock:(void (^)(id json))block;

- (BOOL) InternetStatus:(BOOL) popup;
- (void) PopUpMSG:(NSString *) msg Header:(NSString *) header;
- (void) setDefaultUserData:(NSMutableDictionary *) dict WithKey:(NSString *) key;
- (NSMutableDictionary *) getDefaultUserDataWithKey:(NSString *) key;
- (void) GetDurationWithCurrentLat:(float) cLat CurrentLong:(float) cLong AndPickupLat:(float) pLat PickupLong:(float) pLong ToLabel:(UILabel *) lbl WithExtraText:(NSString *) extra DistToLabel:(UILabel *) lblDist;

@end



