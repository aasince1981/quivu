//
//  NetworkConnection.h
//  Match Model
//
//  Created by Ideal IT Techno on 19/10/13.
//  Copyright (c) 2013 Ideal. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NetworkConnection : NSObject
{
    NSDictionary * dictJSON;
    BOOL Loop;
    BOOL NetPopUp;
}
/*
 - (void) setDefaultUserData:(NSMutableDictionary *) dict WithKey:(NSString *) key;
 - (NSMutableDictionary *) getDefaultUserDataWithKey:(NSString *) key;
 
 - (NSDictionary *) EstablishNetworkConnectionWithWebService:(NSString *) webservice andWithDictionaryInfo:(NSMutableDictionary *) postDict ActivityIndicator:(BOOL) AI;
 - (NSDictionary *) EstablishNetworkConnectionWithWebService:(NSString *) webservice andWithDictionaryInfo:(NSMutableDictionary *) postDict ActivityIndicator:(BOOL) AI WithNetMessage:(BOOL) netMessage;
 
 - (NSDictionary *) EstablishNetworkConnection:(NSString *) url ActivityIndicator:(BOOL) AI;
 - (NSDictionary *) EstablishNetworkConnection:(NSString *) url ActivityIndicator:(BOOL) AI WithNetMessage:(BOOL) netMessage;
 
 - (void) setImage:(UIImageView *) imageView URL:(NSString *) url ActivityIndicator:(UIActivityIndicatorView *) AI AIVisible:(BOOL) AIVisible PlaceholderImage:(NSString *) placeholderImageUrl Button:(UIButton *) button ButtonBackground:(BOOL) isButtonBackground;
 
 - (UIImage *) getImage:(NSString *) url;
 - (UIImage *) getImage:(NSString *) url defaultImage:(NSString *) strImage;
 - (UIImage *) getImage:(NSString *) url defaultImage:(NSString *) strImage WithAI:(UIActivityIndicatorView *) AI;
 
 - (void) SetImageWithOriginalSizeToButton:(NSArray *) arr;
 - (void) SetImageWithOriginalSizeToImageView:(NSArray *) arr;
 - (void) SetImageWithoutSaveWithURL:(NSString *) URL TO:(id) view;
 - (void) SetImageWithoutSaveWithSizeWithURL:(NSString *) URL TO:(id) view WithDefaultImage:(NSString *) defaultImage;
 
 - (void) SetImageWithSaveWithSizeWithURL:(NSString *) URL TO:(id) view WithDefaultImage:(NSString *) defaultImage;
 
 - (void) SetImageWithSaveWithoutSizeWithURL:(NSString *) URL TO:(id) view WithDefaultImage:(NSString *) strImage InBackGround:(BOOL) boolBackGround;
 
 - (void) SetImageWithSaveWithSizeToImageWithURL:(NSString *) URL TO:(id) view WithDefaultImage:(NSString *) defaultImage;
 
 - (void) SetImageFromSizeWithSaveWithURL:(NSString *) URL TO:(id) view WithDefaultImage:(NSString *) strImage InBackGround:(BOOL) boolBackGround;
 
 - (void) SetImageFromSizeWithoutSaveWithURL:(NSString *) URL TO:(id) view WithDefaultImage:(NSString *) strImage InBackGround:(BOOL) boolBackGround;
 */

- (void) GetDurationWithCurrentLat:(float) cLat CurrentLong:(float) cLong AndPickupLat:(float) pLat PickupLong:(float) pLong ToLabel:(UILabel *) lbl WithExtraText:(NSString *) extra DistToLabel:(UILabel *) lblDist;

- (void) setDefaultUserData:(NSMutableDictionary *) dict WithKey:(NSString *) key;
- (NSMutableDictionary *) getDefaultUserDataWithKey:(NSString *) key;

- (NSDictionary *) EstablishNetworkConnectionWithWebService:(NSString *) webservice andWithDictionaryInfo:(NSMutableDictionary *) postDict ActivityIndicator:(BOOL) AI;
- (NSDictionary *) EstablishNetworkConnectionWithWebService:(NSString *) webservice andWithDictionaryInfo:(NSMutableDictionary *) postDict ActivityIndicator:(BOOL) AI WithNetMessage:(BOOL) netMessage;

- (NSDictionary *) EstablishNetworkConnection:(NSString *) url ActivityIndicator:(BOOL) AI;
- (NSDictionary *) EstablishNetworkConnection:(NSString *) url ActivityIndicator:(BOOL) AI WithNetMessage:(BOOL) netMessage;

- (void) setImage:(UIImageView *) imageView URL:(NSString *) url ActivityIndicator:(UIActivityIndicatorView *) AI AIVisible:(BOOL) AIVisible PlaceholderImage:(NSString *) placeholderImageUrl Button:(UIButton *) button ButtonBackground:(BOOL) isButtonBackground;

- (UIImage *) getImage:(NSString *) url;
- (UIImage *) getImage:(NSString *) url defaultImage:(NSString *) strImage;
- (UIImage *) getImage:(NSString *) url defaultImage:(NSString *) strImage WithAI:(UIActivityIndicatorView *) AI;

- (void) SetImageWithOriginalSizeToButton:(NSArray *) arr;
- (void) SetImageWithOriginalSizeToImageView:(NSArray *) arr;
- (void) SetImageWithoutSaveWithURL:(NSString *) URL TO:(id) view;
- (void) SetImageWithoutSaveWithSizeWithURL:(NSString *) URL TO:(id) view WithDefaultImage:(NSString *) defaultImage;

- (void) SetImageWithSaveWithSizeWithURL:(NSString *) URL TO:(id) view WithDefaultImage:(NSString *) defaultImage;

- (void) SetImageWithSaveWithoutSizeWithURL:(NSString *) URL TO:(id) view WithDefaultImage:(NSString *) strImage InBackGround:(BOOL) boolBackGround;

- (void) SetImageWithSaveWithSizeToImageWithURL:(NSString *) URL TO:(id) view WithDefaultImage:(NSString *) defaultImage;

- (void) SetImageWithSaveWithSizeWithURL:(NSString *) URL TO:(id) view WithDefaultImage:(NSString *) defaultImage WithAI:(UIActivityIndicatorView *) myAI;

- (void) SetImageWithSaveWithoutSizeWithURL:(NSString *) URL TO:(id) view WithDefaultImage:(NSString *) strImage InBackGround:(BOOL) boolBackGround WithAI:(UIActivityIndicatorView *) myAI;

- (void) SetImageFromSizeWithSaveWithURL:(NSString *) URL TO:(id) view WithDefaultImage:(NSString *) strImage InBackGround:(BOOL) boolBackGround WithAI:(UIActivityIndicatorView *) myAI;
- (void) SetImageFromSizeWithoutSaveWithURL:(NSString *) URL TO:(id) view WithDefaultImage:(NSString *) strImage InBackGround:(BOOL) boolBackGround WithAI:(UIActivityIndicatorView *) myAI;


@end
