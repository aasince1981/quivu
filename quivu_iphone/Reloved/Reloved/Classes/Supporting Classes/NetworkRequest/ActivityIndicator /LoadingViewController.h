
//
//  LoadingViewController.h
//  Editure
//
//  Created by nilesh on 06/02/12.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"

#define kGPS @"Please establish GPS."
#define kInternetNotAvailable @"Please establish network connection."
#define kCouldnotconnect @"Could not connect to the server. Please try again later."
#define KAppDelegate ((AppDelegate *)[[UIApplication sharedApplication] delegate])

/**
 *	@Class: LoadingViewController
 *	@Description: LoadingViewController class use for implement loading indicator on request start and stop.
 */
@interface LoadingViewController : UIViewController {
    IBOutlet UIActivityIndicatorView *spinner;
}

+(LoadingViewController *) instance;

/**
 * use for start rotaing activity indicator
 * @param: nil
 * @return: IBAction
 */
-(IBAction)startRotation;

/**
 * use for start rotaing activity indicator
 * @param: nil
 * @return: void
 */
-(void)stopRotation;
@end
