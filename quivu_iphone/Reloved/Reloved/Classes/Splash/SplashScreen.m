//
//  SplashScreen.m
//  quivu
//
//  Created by Kamal on 18/06/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "SplashScreen.h"
#import "WelcomeScreenViewController.h"

@interface SplashScreen ()

@end

@implementation SplashScreen

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    if(IS_IPHONE_5) {
        imgSplash.image = [UIImage imageNamed:@"Splash_568.png"];
    } else {
        imgSplash.image = [UIImage imageNamed:@"Splash_480.png"];
    }
    
    timer = [NSTimer scheduledTimerWithTimeInterval:4.0 target:self selector:@selector(Splash) userInfo:nil repeats:YES];
    time = 0;
}

- (void) viewWillAppear:(BOOL)animated {
    KAppDelegate.MyVC = self;
}

- (void) Splash {
    
        @try {
            NSMutableDictionary * dict = [obNet getDefaultUserDataWithKey:Key_LoginDetail];
            
            if(dict.count > 0) {
                KAppDelegate.dictLoginInfo = dict;
                [KAppDelegate TabBarShow];
                
            } else {
                WelcomeScreenViewController *vc = [[WelcomeScreenViewController alloc] initWithNibName:@"WelcomeScreenViewController" bundle:nil];
                [self.navigationController pushViewController:vc animated:NO];
            }
        } @catch (NSException *exception) {}
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

-(void)viewWillDisappear:(BOOL)animated {
    [timer invalidate];
}

@end
