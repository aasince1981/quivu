//
//  NotificationScreen.m
//  Reloved
//
//  Created by Kamal on 29/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "NotificationScreen.h"
#import "ActivityViewController.h"

@interface NotificationScreen ()
{
    NSString * UserId;
    NSMutableDictionary * dictNoti;
}

@end

@implementation NotificationScreen

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
    [self setFontType];
    UserId = @"";
    
    sv_ScrollView.contentSize = CGSizeMake(320, 700);
    sv_ScrollView.delegate = self;
    
    NSUserDefaults * defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:@"1" forKey:@"vibrate"];
    [defaults setObject:@"1" forKey:@"light"];
    [defaults setObject:@"1" forKey:@"sound"];
    [defaults synchronize];
}

- (void) setFontType {
    lblHeader.font = FONT_Lato_Bold(20.0f);
    btnVibrate.titleLabel.font = FONT_Lato_Bold(16.0f);
    btnLight.titleLabel.font = FONT_Lato_Bold(16.0f);
    btnSound.titleLabel.font = FONT_Lato_Bold(16.0f);
    btnItemListed.titleLabel.font = FONT_Lato_Bold(16.0f);
    btnNewOffer.titleLabel.font = FONT_Lato_Bold(16.0f);
    btnNewChat.titleLabel.font = FONT_Lato_Bold(16.0f);
    btnNewComment.titleLabel.font = FONT_Lato_Bold(16.0f);
    btnNewOfferPushNoti.titleLabel.font = FONT_Lato_Bold(16.0f);
    btnNewChatPushNoti.titleLabel.font = FONT_Lato_Bold(16.0f);
}

#pragma mark - viewWillAppear Method
- (void) viewWillAppear:(BOOL)animated {
    KAppDelegate.MyVC = self;
    if(KAppDelegate.dictLoginInfo.count > 0) {
        UserId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

#pragma mark - BtnVibrate actions
- (IBAction)btnVibrate:(id)sender {
    NSUserDefaults * defaults = [NSUserDefaults standardUserDefaults];
    
    NSString * status = @"1";

    if(boolVibrate) {
        [defaults setObject:@"1" forKey:@"vibrate"];
        [defaults synchronize];
        
        [imgVibrateCheckBox setImage:[UIImage imageNamed:@"checkselect_setting.png"]];
        status = @"1";
       [self GetNotificationForVibrate:status];
        
    } else {
        [defaults setObject:@"0" forKey:@"vibrate"];
        [defaults synchronize];
        
        [imgVibrateCheckBox setImage:[UIImage imageNamed:@"checkbox_setting.png"]];
        status = @"0";
        [self GetNotificationForVibrate:status];
    }
    
    boolVibrate = !boolVibrate;
}

- (void) GetNotificationForVibrate: (NSString *) userStatus {
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&UserId=%@&UserVibrateStatus=%@",[KAppDelegate getDictServer:WS_Setting],UserId,userStatus];
        NSLog(@"Setting Vibrate Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            NSLog(@"Setting Vibrate json value--> %@",json);
            
            if ([[json objectForKey:@"success"] intValue] == 1) {
                
            } else {
                [[[UIAlertView alloc] initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil] show];
            }
        }];
    }
}

#pragma mark - BtnLight actions
- (IBAction)btnLight:(id)sender {
    NSUserDefaults * defaults = [NSUserDefaults standardUserDefaults];
    
    NSString * status = @"1";
    
    if(boolLight) {
        [defaults setObject:@"1" forKey:@"light"];
        [defaults synchronize];
        
        [imgLightCheckBox setImage:[UIImage imageNamed:@"checkselect_setting.png"]];
        status = @"1";
        [self GetNotificationForLight:status];
        
    } else {
        [defaults setObject:@"0" forKey:@"light"];
        [defaults synchronize];
        
        [imgLightCheckBox setImage:[UIImage imageNamed:@"checkbox_setting.png"]];
        status = @"0";
        [self GetNotificationForLight:status];
    }
    
    boolLight = !boolLight;
}

- (void) GetNotificationForLight: (NSString *) userStatus {
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&UserId=%@&UserLightStatus=%@",[KAppDelegate getDictServer:WS_Setting],UserId,userStatus];
        NSLog(@"Setting Light Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            NSLog(@"Setting Light json value--> %@",json);
            
            if ([[json objectForKey:@"success"] intValue] == 1) {
                
            } else {
                [[[UIAlertView alloc] initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil] show];
            }
        }];
    }
}

#pragma mark - BtnSound actions
- (IBAction)btnSound:(id)sender {
    
    NSUserDefaults * defaults = [NSUserDefaults standardUserDefaults];
    
    NSString * status = @"1";
    
    if(boolSound) {
        [defaults setObject:@"1" forKey:@"sound"];
        [defaults synchronize];
        
        [imgSoundCheckBox setImage:[UIImage imageNamed:@"checkselect_setting.png"]];
        status = @"1";
        [self GetNotificationForSound:status];
        
    } else {
        [defaults setObject:@"0" forKey:@"sound"];
        [defaults synchronize];

        [imgSoundCheckBox setImage:[UIImage imageNamed:@"checkbox_setting.png"]];
        status = @"0";
        [self GetNotificationForSound:status];
    }
    
    boolSound = !boolSound;
}

- (void) GetNotificationForSound: (NSString *) userStatus {
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&UserId=%@&UserSoundStatus=%@",[KAppDelegate getDictServer:WS_Setting],UserId,userStatus];
        NSLog(@"Setting Sound Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            NSLog(@"Setting Sound json value--> %@",json);
            
            if ([[json objectForKey:@"success"] intValue] == 1) {
                
            } else {
                [[[UIAlertView alloc] initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil] show];
            }
        }];
    }
}

#pragma mark - BtnItemListed actions
- (IBAction)btnItemListed:(id)sender {
    
    NSString * status = @"1";
    
    if(boolItemListed) {
        [imgItemListedCheckBox setImage:[UIImage imageNamed:@"checkselect_setting.png"]];
        status = @"1";
        [self GetNotificationForItemListed:status];
        
    } else {
        [imgItemListedCheckBox setImage:[UIImage imageNamed:@"checkbox_setting.png"]];
        status = @"0";
        [self GetNotificationForItemListed:status];
    }
    
    boolItemListed = !boolItemListed;
}

- (void) GetNotificationForItemListed: (NSString *) userStatus {
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&UserId=%@&UserItemListStatus=%@",[KAppDelegate getDictServer:WS_Setting],UserId,userStatus];
        NSLog(@"Setting ItemListed Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            NSLog(@"Setting ItemListed json value--> %@",json);
            
            if ([[json objectForKey:@"success"] intValue] == 1) {
                
            } else {
                [[[UIAlertView alloc] initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil] show];
            }
        }];
    }
}

#pragma mark - BtnNewOffer actions
- (IBAction)btnNewOffer:(id)sender {
    NSString * status = @"1";
    
    if(boolNewOffer) {
        [imgNewOfferCheckBox setImage:[UIImage imageNamed:@"checkselect_setting.png"]];
        status = @"1";
        [self GetNotificationForNewOffer:status];
        
    } else {
        [imgNewOfferCheckBox setImage:[UIImage imageNamed:@"checkbox_setting.png"]];
        status = @"0";
        [self GetNotificationForNewOffer:status];
    }
    
    boolNewOffer = !boolNewOffer;
}

- (void) GetNotificationForNewOffer: (NSString *) userStatus {
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&UserId=%@&UserENewOfferStatus=%@",[KAppDelegate getDictServer:WS_Setting],UserId,userStatus];
        NSLog(@"Setting NewOffer Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            NSLog(@"Setting NewOffer json value--> %@",json);
            
            if ([[json objectForKey:@"success"] intValue] == 1) {
                
            } else {
                [[[UIAlertView alloc] initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil] show];
            }
        }];
    }
}

#pragma mark - BtnNewChat actions
- (IBAction)btnNewChat:(id)sender {
    
    NSString * status = @"1";
    
    if(boolNewChat) {
        [imgNewChatCheckBox setImage:[UIImage imageNamed:@"checkselect_setting.png"]];
        status = @"1";
        [self GetNotificationForNewChat:status];
        
    } else {
        [imgNewChatCheckBox setImage:[UIImage imageNamed:@"checkbox_setting.png"]];
        status = @"0";
        [self GetNotificationForNewChat:status];
    }
    
    boolNewChat = !boolNewChat;
}

- (void) GetNotificationForNewChat: (NSString *) userStatus {
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&UserId=%@&UserENewChatStatus=%@",[KAppDelegate getDictServer:WS_Setting],UserId,userStatus];
        NSLog(@"Setting NewChat Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            NSLog(@"Setting NewChat json value--> %@",json);
            
            if ([[json objectForKey:@"success"] intValue] == 1) {
                
            } else {
                [[[UIAlertView alloc] initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil] show];
            }
        }];
    }
}

#pragma mark - BtnNewComment actions
- (IBAction)btnNewComment:(id)sender {
    
    NSString * status = @"1";
    
    if(boolNewComment) {
        [imgNewCommentCheckBox setImage:[UIImage imageNamed:@"checkselect_setting.png"]];
        status = @"1";
        [self GetNotificationForNewComment:status];
        
    } else {
        [imgNewCommentCheckBox setImage:[UIImage imageNamed:@"checkbox_setting.png"]];
        status = @"0";
        [self GetNotificationForNewComment:status];
    }
    
    boolNewComment = !boolNewComment;
}

- (void) GetNotificationForNewComment: (NSString *) userStatus {
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&UserId=%@&UserPNCommentStatus=%@",[KAppDelegate getDictServer:WS_Setting],UserId,userStatus];
        NSLog(@"Setting NewComment Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            NSLog(@"Setting NewComment json value--> %@",json);
            
            if ([[json objectForKey:@"success"] intValue] == 1) {
                
            } else {
                [[[UIAlertView alloc] initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil] show];
            }
        }];
    }
}

#pragma mark - BtnNewOfferPushNoti actions
- (IBAction)btnNewOfferPushNoti:(id)sender {
    
    NSString * status = @"1";
    
    if(boolNewOfferPushNoti) {
        [imgNewOfferPushNotiCheckBox setImage:[UIImage imageNamed:@"checkselect_setting.png"]];
        status = @"1";
        [self GetNotificationForNewOfferPushNoti:status];
        
    } else {
        [imgNewOfferPushNotiCheckBox setImage:[UIImage imageNamed:@"checkbox_setting.png"]];
        status = @"0";
        [self GetNotificationForNewOfferPushNoti:status];
    }
    
    boolNewOfferPushNoti = !boolNewOfferPushNoti;
}

- (void) GetNotificationForNewOfferPushNoti: (NSString *) userStatus {
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&UserId=%@&UserPNOfferStatus=%@",[KAppDelegate getDictServer:WS_Setting],UserId,userStatus];
        NSLog(@"Setting UserPNOfferStatus Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            NSLog(@"Setting UserPNOfferStatus json value--> %@",json);
            
            if ([[json objectForKey:@"success"] intValue] == 1) {
                
            } else {
                [[[UIAlertView alloc] initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil] show];
            }
        }];
    }
}

#pragma mark - BtnNewChatPushNoti actions
- (IBAction)btnNewChatPushNoti:(id)sender {
    
    NSString * status = @"1";
    
    if(boolNewChatPushNoti) {
        [imgNewChatPushNotiCheckBox setImage:[UIImage imageNamed:@"checkselect_setting.png"]];
        status = @"1";
        [self GetNotificationForNewChatPushNoti:status];
        
    } else {
        [imgNewChatPushNotiCheckBox setImage:[UIImage imageNamed:@"checkbox_setting.png"]];
        status = @"0";
        [self GetNotificationForNewChatPushNoti:status];
    }
    
    boolNewChatPushNoti = !boolNewChatPushNoti;
}

- (void) GetNotificationForNewChatPushNoti: (NSString *) userStatus {
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&UserId=%@&UserPNChatStatus=%@",[KAppDelegate getDictServer:WS_Setting],UserId,userStatus];
        NSLog(@"Setting UserPNOfferStatus Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            NSLog(@"Setting UserPNOfferStatus json value--> %@",json);
            
            if ([[json objectForKey:@"success"] intValue] == 1) {
                
            } else {
                [[[UIAlertView alloc] initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil] show];
            }
        }];
    }
}

- (IBAction)btnBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

@end



