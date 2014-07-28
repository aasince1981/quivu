//
//  NetworkConnection.m
//  Match Model
//
//  Created by Ideal IT Techno on 19/10/13.
//  Copyright (c) 2013 Ideal. All rights reserved.
//

#import "NetworkConnection.h"

@interface NetworkConnection ()
@property (strong, nonatomic) UIImageView * IVimageView;
@property (strong, nonatomic) NSString * IVurl;
@property (strong, nonatomic) UIActivityIndicatorView * IVAI;
@property BOOL IVAIVisible;
@property (strong, nonatomic) NSString * IVplaceholderImageUrl;

@property (strong, nonatomic) UIButton * IVButton;
@property BOOL IVisButtonBackground;
@end

//#define kInternetNotAvailable @"Please establish network connection."
//#define kCouldnotconnect @"Could not connect to the server. Please try again later."

@implementation NetworkConnection
@synthesize IVimageView, IVurl, IVAI, IVAIVisible, IVplaceholderImageUrl, IVButton, IVisButtonBackground;

- (NSDictionary *) EstablishNetworkConnection:(NSString *) url ActivityIndicator:(BOOL) AI {
    NetPopUp = YES;
    if (AI)
        [self performSelectorInBackground:@selector(StartAI) withObject:self];
    
    if ([obNet InternetStatus:NO]) {
        [self EstablishNetworkConnectionThread:url];
    } else {
        UIAlertView * alert = [[UIAlertView alloc] initWithTitle:@"Network Message" message:kInternetNotAvailable delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
        [alert show];
    }
     
    while (Loop)
        usleep(10000);
    
    return dictJSON;
}

- (NSDictionary *) EstablishNetworkConnection:(NSString *) url ActivityIndicator:(BOOL) AI WithNetMessage:(BOOL) netMessage{
    NetPopUp = netMessage;
    if (AI)
        [self performSelectorInBackground:@selector(StartAI) withObject:self];
    
    if ([obNet InternetStatus:NO]) {
        [self EstablishNetworkConnectionThread:url];
    } else if(netMessage) {
        UIAlertView * alert = [[UIAlertView alloc] initWithTitle:@"Network Message" message:kInternetNotAvailable delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
        [alert show];
    }
    
    while (Loop)
        usleep(10000);
    
    return dictJSON;
}

- (void) EstablishNetworkConnectionThread:(NSString *) url{
    @try {
        url=[url stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        
        NSString * API = url;
        //NSLog(@"NetworkConnection = %@", API);
        NSString *post = [NSString stringWithFormat:@""];
        NSData *postData = [post dataUsingEncoding:NSASCIIStringEncoding allowLossyConversion:YES];
        
        NSString *postLength = [NSString stringWithFormat:@"%lu", (unsigned long)[postData length]];
        
        NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
        [request setURL:[NSURL URLWithString:API]];
        [request setHTTPMethod:@"POST"];
        [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
        [request setValue:@"application/x-www-form-urlencoded;charset=UTF-8" forHTTPHeaderField:@"Content-Type"];
        [request setHTTPBody:postData];
        [request setTimeoutInterval:20.0];
        
        NSURLResponse *response;
        NSData *POSTReply = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:nil];
        NSString *theReply = [[NSString alloc] initWithBytes:[POSTReply bytes] length:[POSTReply length] encoding: NSASCIIStringEncoding];
        
        NSError * e;
        dictJSON = [NSJSONSerialization JSONObjectWithData: [theReply dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers error: &e];
        //NSLog(@"NetworkConnection JSON = %@", dictJSON);
        
        if (dictJSON == nil) {
            NSLog(@"Web Service Error url = %@\nError = %@", url, theReply);
            [self performSelectorOnMainThread:@selector(PopUpNetworkMessage) withObject:nil waitUntilDone:NO];
        }
        
        Loop = NO;
        [[LoadingViewController instance] stopRotation];
    } @catch (NSException *exception) {
        [[LoadingViewController instance] stopRotation];
    } @finally { }
}

- (void) PopUpNetworkMessage
{
    if (NetPopUp) {
        UIAlertView * alert = [[UIAlertView alloc] initWithTitle:@"Network Message" message:kCouldnotconnect delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
        [alert show];
    }
}

- (void) StartAI
{
    [self performSelectorInBackground:@selector(StartAIThread) withObject:self];
}

- (void) StartAIThread
{
    [[LoadingViewController instance] startRotation];
}

- (void) setImage:(UIImageView *) imageView URL:(NSString *) url ActivityIndicator:(UIActivityIndicatorView *) AI AIVisible:(BOOL) AIVisible PlaceholderImage:(NSString *) placeholderImageUrl Button:(UIButton *) button ButtonBackground:(BOOL) isButtonBackground
{
    IVimageView =imageView;
    IVurl = url;
    IVAI = AI;
    IVAIVisible = AIVisible;
    IVplaceholderImageUrl = placeholderImageUrl;
    IVButton = button;
    IVisButtonBackground = isButtonBackground;
    [self performSelectorInBackground:@selector(setImage) withObject:self];
}

- (void) IVAIStartThread
{
    if (IVAI && IVAIVisible) {
        [IVAI startAnimating];
        [IVAI setHidden:NO];
    }
}

- (void) IVAIStopThread
{
    if (IVAI && IVAIVisible) {
        [IVAI stopAnimating];
        [IVAI setHidden:YES];
    }
}

- (void) setImage
{
    if (IVAI && IVAIVisible)
        [self performSelectorInBackground:@selector(IVAIStartThread) withObject:self];
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    
    UIImage * image = [UIImage imageWithData:[defaults objectForKey:IVurl]];
    
    if (image == nil) {
        NSData * data = [NSData dataWithContentsOfURL:[NSURL URLWithString:IVurl]];
        image = [UIImage imageWithData:data];
        NSData *imageData = UIImageJPEGRepresentation(image, 1.0);
        [defaults setValue:imageData forKey:IVurl];
        [defaults synchronize];
    } else {
        if (IVAI != nil) {
            [IVAI stopAnimating];
            [IVAI setHidden:YES];
        }
    }
    
    if (IVButton) {
        if (IVisButtonBackground) {
            [IVButton setBackgroundImage:image forState:UIControlStateNormal];
        } else {
            [IVButton setImage:image forState:UIControlStateNormal];
        }
    } else {
        if (image) {
            [IVimageView setImage:image];
        } else {
            if (IVplaceholderImageUrl)
                [IVimageView setImage:[UIImage imageNamed:IVplaceholderImageUrl]];
        }
    }
    
    if (IVAI && IVAIVisible)
        [self performSelectorInBackground:@selector(IVAIStopThread) withObject:self];
    
    if (IVAI != nil)
        [IVAI removeFromSuperview];
}

- (UIImage *) getImage:(NSString *) url
{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    UIImage * image = nil;
    if ([defaults objectForKey:url] != nil) {
        image = [UIImage imageWithData:[defaults objectForKey:url]];
        if (image == nil) {
            NSData * data = [NSData dataWithContentsOfURL:[NSURL URLWithString:url]];
            image = [UIImage imageWithData:data];
            NSData *imageData = UIImageJPEGRepresentation(image, 1.0);
            [defaults setValue:imageData forKey:url];
            [defaults synchronize];
        }
    }
    
    return image;
}

- (UIImage *) getImage:(NSString *) url defaultImage:(NSString *) strImage
{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    UIImage * image = [UIImage imageWithData:[defaults objectForKey:url]];
    if (image == nil) {
        NSData * data = [NSData dataWithContentsOfURL:[NSURL URLWithString:url]];
        image = [UIImage imageWithData:data];
        NSData *imageData = UIImageJPEGRepresentation(image, 1.0);
        [defaults setValue:imageData forKey:url];
        [defaults synchronize];
    }
    
    if (image == nil) {
        image = [UIImage imageNamed:strImage];
    }
    
    return image;
}

- (void) AnimateAIStart:(UIActivityIndicatorView *) AI
{
    [AI startAnimating];
    [AI setHidden:NO];
}

- (void) AnimateAIStop:(UIActivityIndicatorView *) AI
{
    [AI stopAnimating];
    [AI setHidden:YES];
}

- (UIImage *) getImage:(NSString *) url defaultImage:(NSString *) strImage WithAI:(UIActivityIndicatorView *) AI
{
    [self performSelectorInBackground:@selector(AnimateAIStart:) withObject:AI];
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    UIImage * image = [UIImage imageWithData:[defaults objectForKey:url]];
    if (image == nil) {
        NSData * data = [NSData dataWithContentsOfURL:[NSURL URLWithString:url]];
        image = [UIImage imageWithData:data];
        NSData *imageData = UIImageJPEGRepresentation(image, 1.0);
        [defaults setValue:imageData forKey:url];
        [defaults synchronize];
    }
    
    if (image == nil) {
        image = [UIImage imageNamed:strImage];
    }
    
    [self performSelectorInBackground:@selector(AnimateAIStop:) withObject:AI];
    
    return image;
}

- (void) SetImageWithSaveWithSizeWithURL:(NSString *) URL TO:(id) view WithDefaultImage:(NSString *) defaultImage
{
    UIImage * image = [self getImage:URL defaultImage:defaultImage];
    
    if ([view isKindOfClass:[UIButton class]]) {
        UIButton * button = (UIButton *) view;
        
        [button setImage:image forState:UIControlStateNormal];
        
        CGSize imgSize = image.size;
        
        float ratio=button.frame.size.width/imgSize.width;
        float scaledHeight=imgSize.height*ratio;
        
        float diff = button.frame.size.height - scaledHeight;
        if(scaledHeight < button.frame.size.height) {
            button.frame = CGRectMake(button.frame.origin.x+5, (diff/2)+button.frame.origin.y+5, button.frame.size.width-10, scaledHeight-10);
        }
        
        float ratioW=button.frame.size.height/imgSize.height;
        float scaledw=imgSize.width*ratioW;
        float diffW = button.frame.size.width - scaledw;
        if(scaledw < button.frame.size.width) {
            button.frame = CGRectMake((diffW/2)+button.frame.origin.x+5, button.frame.origin.y+5, scaledw-10, button.frame.size.height-10);
        }
    } else {
        UIImageView * button = (UIImageView *) view;
        [button setImage:image];
        
        CGSize imgSize = image.size;
        
        float ratio=button.frame.size.width/imgSize.width;
        float scaledHeight=imgSize.height*ratio;
        
        float diff = button.frame.size.height - scaledHeight;
        if(scaledHeight < button.frame.size.height) {
            button.frame = CGRectMake(button.frame.origin.x+5, (diff/2)+button.frame.origin.y+5, button.frame.size.width-10, scaledHeight-10);
        }
        
        float ratioW=button.frame.size.height/imgSize.height;
        float scaledw=imgSize.width*ratioW;
        float diffW = button.frame.size.width - scaledw;
        if(scaledw < button.frame.size.width) {
            button.frame = CGRectMake((diffW/2)+button.frame.origin.x+5, button.frame.origin.y+5, scaledw-10, button.frame.size.height-10);
        }
    }
}

- (void) SetImageWithOriginalSizeToImageView:(NSArray *) arr
{
    UIImage * image = [self getImage:[arr objectAtIndex:1] defaultImage:[arr objectAtIndex:2]];
    
    UIImageView * button = [arr objectAtIndex:0];
    //[button setImage:image forState:UIControlStateNormal];
    button.image = image;
    
    CGSize imgSize = image.size;
    
    float ratio=button.frame.size.width/imgSize.width;
    float scaledHeight=imgSize.height*ratio;
    
    float diff = button.frame.size.height - scaledHeight;
    if(scaledHeight < button.frame.size.height) {
        button.frame = CGRectMake(button.frame.origin.x+5, (diff/2)+button.frame.origin.y+5, button.frame.size.width-10, scaledHeight-10);
    }
    
    float ratioW=button.frame.size.height/imgSize.height;
    float scaledw=imgSize.width*ratioW;
    float diffW = button.frame.size.width - scaledw;
    if(scaledw < button.frame.size.width) {
        button.frame = CGRectMake((diffW/2)+button.frame.origin.x+5, button.frame.origin.y+5, scaledw-10, button.frame.size.height-10);
    }
}

- (void) SetImageWithOriginalSizeToButton:(NSArray *) arr
{
    UIImage * image = [self getImage:[arr objectAtIndex:1] defaultImage:[arr objectAtIndex:2]];
    
    UIButton * button = [arr objectAtIndex:0];
    [button setImage:image forState:UIControlStateNormal];
    
    CGSize imgSize = image.size;
    
    float ratio=button.frame.size.width/imgSize.width;
    float scaledHeight=imgSize.height*ratio;
    
    float diff = button.frame.size.height - scaledHeight;
    if(scaledHeight < button.frame.size.height) {
        button.frame = CGRectMake(button.frame.origin.x+5, (diff/2)+button.frame.origin.y+5, button.frame.size.width-10, scaledHeight-10);
    }
    
    float ratioW=button.frame.size.height/imgSize.height;
    float scaledw=imgSize.width*ratioW;
    float diffW = button.frame.size.width - scaledw;
    if(scaledw < button.frame.size.width) {
        button.frame = CGRectMake((diffW/2)+button.frame.origin.x+5, button.frame.origin.y+5, scaledw-10, button.frame.size.height-10);
    }
}

- (void) SetImageWithSaveWithoutSizeWithURL:(NSString *) URL TO:(id) view WithDefaultImage:(NSString *) strImage InBackGround:(BOOL) boolBackGround
{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    UIImage * image = [UIImage imageWithData:[defaults objectForKey:URL]];
    
    if (image == nil) {
        NSURLRequest * request = [NSURLRequest requestWithURL:[NSURL URLWithString:URL]];
        [NSURLConnection sendAsynchronousRequest:request
                                           queue:[NSOperationQueue mainQueue]
                               completionHandler:^(NSURLResponse * response, NSData * data, NSError * error)
         {
             UIImage * image;
             if (data != nil) {
                 image = [UIImage imageWithData:data];
             } else {
                 image = [UIImage imageNamed:strImage];
             }
             
             if (image != nil) {
                 NSData *imageData = UIImageJPEGRepresentation(image, 1.0);
                 [defaults setValue:imageData forKey:URL];
                 [defaults synchronize];
             }
         }];
    } else {
        
        if ([view isKindOfClass:[UIButton class]]) {
            UIButton * btn = (UIButton *) view;
            if (boolBackGround) {
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

- (void) SetImageWithoutSaveWithURL:(NSString *) URL TO:(id) view
{
    NSURLRequest * request = [NSURLRequest requestWithURL:[NSURL URLWithString:URL]];
    [NSURLConnection sendAsynchronousRequest:request
                                       queue:[NSOperationQueue mainQueue]
                           completionHandler:^(NSURLResponse * response, NSData * data, NSError * error)
     {
         //[UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
         if (data) {
             UIImage * image = [UIImage imageWithData:data];
             if ([view isKindOfClass:[UIButton class]]) {
                 UIButton * btn = (UIButton *) view;
                 [btn setImage:image forState:UIControlStateNormal];
             } else {
                 UIImageView * img = (UIImageView *) view;
                 [img setImage:image];
             }
         }
     }];
}

- (void) SetImageWithoutSaveWithSizeWithURL:(NSString *) URL TO:(id) view WithDefaultImage:(NSString *) defaultImage
{
    NSURLRequest * request = [NSURLRequest requestWithURL:[NSURL URLWithString:URL]];
    [NSURLConnection sendAsynchronousRequest:request
                                       queue:[NSOperationQueue mainQueue]
                           completionHandler:^(NSURLResponse * response, NSData * data, NSError * error)
     {
         if (data != nil) {
             UIImage * image = [UIImage imageWithData:data];
             if ([view isKindOfClass:[UIButton class]]) {
                 UIButton * button = (UIButton *) view;
                 [button setImage:image forState:UIControlStateNormal];
                 
                 CGSize imgSize = image.size;
                 
                 float ratio=button.frame.size.width/imgSize.width;
                 float scaledHeight=imgSize.height*ratio;
                 
                 float diff = button.frame.size.height - scaledHeight;
                 if(scaledHeight < button.frame.size.height) {
                     button.frame = CGRectMake(button.frame.origin.x+5, (diff/2)+button.frame.origin.y+5, button.frame.size.width-10, scaledHeight-10);
                 }
                 
                 float ratioW=button.frame.size.height/imgSize.height;
                 float scaledw=imgSize.width*ratioW;
                 float diffW = button.frame.size.width - scaledw;
                 if(scaledw < button.frame.size.width) {
                     button.frame = CGRectMake((diffW/2)+button.frame.origin.x+5, button.frame.origin.y+5, scaledw-10, button.frame.size.height-10);
                 }
             } else {
                 UIImageView * button = (UIImageView *) view;
                 [button setImage:image];
                 
                 CGSize imgSize = image.size;
                 
                 float ratio=button.frame.size.width/imgSize.width;
                 float scaledHeight=imgSize.height*ratio;
                 
                 float diff = button.frame.size.height - scaledHeight;
                 if(scaledHeight < button.frame.size.height) {
                     button.frame = CGRectMake(button.frame.origin.x+5, (diff/2)+button.frame.origin.y+5, button.frame.size.width-10, scaledHeight-10);
                 }
                 
                 float ratioW=button.frame.size.height/imgSize.height;
                 float scaledw=imgSize.width*ratioW;
                 float diffW = button.frame.size.width - scaledw;
                 if(scaledw < button.frame.size.width) {
                     button.frame = CGRectMake((diffW/2)+button.frame.origin.x+5, button.frame.origin.y+5, scaledw-10, button.frame.size.height-10);
                 }
             }
         } else {
             UIImage * image = [UIImage imageNamed:defaultImage];
             if ([view isKindOfClass:[UIButton class]]) {
                 UIButton * button = (UIButton *) view;
                 [button setImage:image forState:UIControlStateNormal];
             } else {
                 UIImageView * button = (UIImageView *) view;
                 [button setImage:image];
             }
         }
     }];
}

- (void) SetImageWithSaveWithSizeToImageWithURL:(NSString *) URL TO:(id) view WithDefaultImage:(NSString *) defaultImage
{
    UIImage * image = [self getImage:URL defaultImage:defaultImage];
    
    if ([view isKindOfClass:[UIButton class]]) {
        UIButton * button = (UIButton *) view;
        CGRect rect = button.frame;
        
        [button setImage:image forState:UIControlStateNormal];
        
        CGSize imgSize = image.size;
        
        float ratio=button.frame.size.width/imgSize.width;
        float scaledHeight=imgSize.height*ratio;
        float diff = button.frame.size.height - scaledHeight;
        
        if(scaledHeight < button.frame.size.height) {
            button.frame = CGRectMake(button.frame.origin.x+5, (diff/2)+button.frame.origin.y+5, button.frame.size.width-10, scaledHeight-10);
        }
        
        float ratioW=button.frame.size.height/imgSize.height;
        float scaledw=imgSize.width*ratioW;
        float diffW = button.frame.size.width - scaledw;
        
        if(scaledw < button.frame.size.width) {
            button.frame = CGRectMake((diffW/2)+button.frame.origin.x+5, button.frame.origin.y+5, scaledw-10, button.frame.size.height-10);
        }
        
        NSData *imageData = UIImagePNGRepresentation(image);
        UIImage * ii = [UIImage imageWithData:imageData scale:((int)(((scaledHeight/diff) >= 0)?(scaledHeight/diff):-(scaledHeight/diff)))+1];
        [button setImage:ii forState:UIControlStateNormal];
        
        button.frame = rect;
    } else {
        UIImageView * button = (UIImageView *) view;
        
        CGRect rect = button.frame;
        
        [button setImage:image];
        
        CGSize imgSize = image.size;
        
        float ratio=button.frame.size.width/imgSize.width;
        float scaledHeight=imgSize.height*ratio;
        
        float diff = button.frame.size.height - scaledHeight;
        if(scaledHeight < button.frame.size.height) {
            button.frame = CGRectMake(button.frame.origin.x+5, (diff/2)+button.frame.origin.y+5, button.frame.size.width-10, scaledHeight-10);
        }
        
        float ratioW=button.frame.size.height/imgSize.height;
        float scaledw=imgSize.width*ratioW;
        float diffW = button.frame.size.width - scaledw;
        if(scaledw < button.frame.size.width) {
            button.frame = CGRectMake((diffW/2)+button.frame.origin.x+5, button.frame.origin.y+5, scaledw-10, button.frame.size.height-10);
        }
        
        NSData *imageData = UIImagePNGRepresentation(image);
        UIImage * ii = [UIImage imageWithData:imageData scale:((int)(((scaledHeight/diff) >= 0)?(scaledHeight/diff):-(scaledHeight/diff)))+1];
        [button setImage:ii];
        
        button.frame = rect;
    }
}

- (void) UploadImage:(UIImage *) imageD {
    NSData *imageData = UIImageJPEGRepresentation([UIImage imageNamed:@""],0.2);     //change Image to NSData
    NSString * filenames;
    if (imageData != nil)
    {
        filenames = [NSString stringWithFormat:@"TextLabel"];      //set name here
        NSLog(@"%@", filenames);
        NSString *urlString = @"http://xxxxxxx/yyyyy.php";
        
        NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
        [request setURL:[NSURL URLWithString:urlString]];
        [request setHTTPMethod:@"POST"];
        
        NSString *boundary = @"---------------------------14737809831466499882746641449";
        NSString *contentType = [NSString stringWithFormat:@"multipart/form-data; boundary=%@",boundary];
        [request addValue:contentType forHTTPHeaderField: @"Content-Type"];
        
        NSMutableData *body = [NSMutableData data];
        
        [body appendData:[[NSString stringWithFormat:@"\r\n--%@\r\n",boundary] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"filenames\"\r\n\r\n"] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[filenames dataUsingEncoding:NSUTF8StringEncoding]];
        
        [body appendData:[[NSString stringWithFormat:@"\r\n--%@\r\n",boundary] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[@"Content-Disposition: form-data; name=\"userfile\"; filename=\".jpg\"\r\n" dataUsingEncoding:NSUTF8StringEncoding]];
        
        [body appendData:[@"Content-Type: application/octet-stream\r\n\r\n" dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[NSData dataWithData:imageData]];
        [body appendData:[[NSString stringWithFormat:@"\r\n--%@--\r\n",boundary] dataUsingEncoding:NSUTF8StringEncoding]];
        // setting the body of the post to the reqeust
        [request setHTTPBody:body];
        // now lets make the connection to the web
        NSData *returnData = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
        NSString *returnString = [[NSString alloc] initWithData:returnData encoding:NSUTF8StringEncoding];
        NSLog(@"%@", returnString);
        NSLog(@"finish");
    }
}

- (NSDictionary *) EstablishNetworkConnectionWithWebService:(NSString *) webservice andWithDictionaryInfo:(NSMutableDictionary *) postDict ActivityIndicator:(BOOL) AI {
    NetPopUp = YES;
    if (AI)
        [self performSelectorInBackground:@selector(StartAI) withObject:self];
    
    if ([obNet InternetStatus:NO]) {
        //[self EstablishNetworkConnectionThread:url];
        [self EstablishNetworkConnectionWithWebService:webservice andWithDictionaryInfo:postDict];
    } else {
        UIAlertView * alert = [[UIAlertView alloc] initWithTitle:@"Network Message" message:kInternetNotAvailable delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
        [alert show];
    }
    
    while (Loop)
        usleep(10000);
    
    return dictJSON;
}

- (NSDictionary *) EstablishNetworkConnectionWithWebService:(NSString *) webservice andWithDictionaryInfo:(NSMutableDictionary *) postDict ActivityIndicator:(BOOL) AI WithNetMessage:(BOOL) netMessage {
    NetPopUp = netMessage;
    if (AI)
        [self performSelectorInBackground:@selector(StartAI) withObject:self];
    
    if ([obNet InternetStatus:NO]) {
        [self EstablishNetworkConnectionWithWebService:webservice andWithDictionaryInfo:postDict];
    } else if(netMessage) {
        UIAlertView * alert = [[UIAlertView alloc] initWithTitle:@"Network Message" message:kInternetNotAvailable delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
        [alert show];
    }
    
    while (Loop)
        usleep(10000);
    
    return dictJSON;
}

- (void) EstablishNetworkConnectionWithWebService:(NSString *) webservice andWithDictionaryInfo:(NSMutableDictionary *) postDict {
    @try {
        NSURL *url = [NSURL URLWithString:webservice];
        
        NSLog(@"postDict-%@", postDict);
        NSData *postData = [self encodeDictionary:postDict];
        
        // Create the request
        NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url];
        [request setHTTPMethod:@"POST"];
        [request setValue:[NSString stringWithFormat:@"%d", postData.length] forHTTPHeaderField:@"Content-Length"];
        [request setValue:@"application/x-www-form-urlencoded charset=utf-8" forHTTPHeaderField:@"Content-Type"];
        [request setHTTPBody:postData];
        
        NSURLResponse *response;
        NSData *POSTReply = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:nil];
        NSString *theReply = [[NSString alloc] initWithBytes:[POSTReply bytes] length:[POSTReply length] encoding: NSASCIIStringEncoding];
        
        NSError * e;
        dictJSON = [NSJSONSerialization JSONObjectWithData: [theReply dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers error: &e];
        //NSLog(@"NetworkConnection JSON = %@", dictJSON);
        
        if (dictJSON == nil) {
            NSLog(@"Web Service Error url = %@\nError = %@", url, theReply);
            [self performSelectorOnMainThread:@selector(PopUpNetworkMessage) withObject:nil waitUntilDone:NO];
        }
        
        Loop = NO;
        [[LoadingViewController instance] stopRotation];
    } @catch (NSException *exception) {
        [[LoadingViewController instance] stopRotation];
    } @finally { }
}

- (NSData*)encodeDictionary:(NSDictionary*)dictionary {
    NSMutableArray *parts = [[NSMutableArray alloc] init];
    for (NSString *key in dictionary) {
        NSString *encodedValue = [[dictionary objectForKey:key] stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        NSString *encodedKey = [key stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        NSString *part = [NSString stringWithFormat: @"%@=%@", encodedKey, encodedValue];
        [parts addObject:part];
    }
    
    NSString *encodedDictionary = [parts componentsJoinedByString:@"&"];
    return [encodedDictionary dataUsingEncoding:NSUTF8StringEncoding];
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

- (void) SetImageWithSaveWithSizeWithURL:(NSString *) URL TO:(id) view WithDefaultImage:(NSString *) defaultImage WithAI:(UIActivityIndicatorView *) myAI
{
    UIImage * image = [self getImage:URL defaultImage:defaultImage];
    
    if ([view isKindOfClass:[UIButton class]]) {
        UIButton * button = (UIButton *) view;
        
        [button setImage:image forState:UIControlStateNormal];
        
        CGSize imgSize = image.size;
        
        float ratio=button.frame.size.width/imgSize.width;
        float scaledHeight=imgSize.height*ratio;
        
        float diff = button.frame.size.height - scaledHeight;
        if(scaledHeight < button.frame.size.height) {
            button.frame = CGRectMake(button.frame.origin.x+5, (diff/2)+button.frame.origin.y+5, button.frame.size.width-10, scaledHeight-10);
        }
        
        float ratioW=button.frame.size.height/imgSize.height;
        float scaledw=imgSize.width*ratioW;
        float diffW = button.frame.size.width - scaledw;
        if(scaledw < button.frame.size.width) {
            button.frame = CGRectMake((diffW/2)+button.frame.origin.x+5, button.frame.origin.y+5, scaledw-10, button.frame.size.height-10);
        }
        if (myAI != nil)
            [myAI removeFromSuperview];
    } else {
        UIImageView * button = (UIImageView *) view;
        [button setImage:image];
        
        CGSize imgSize = image.size;
        
        float ratio=button.frame.size.width/imgSize.width;
        float scaledHeight=imgSize.height*ratio;
        
        float diff = button.frame.size.height - scaledHeight;
        if(scaledHeight < button.frame.size.height) {
            button.frame = CGRectMake(button.frame.origin.x+5, (diff/2)+button.frame.origin.y+5, button.frame.size.width-10, scaledHeight-10);
        }
        
        float ratioW=button.frame.size.height/imgSize.height;
        float scaledw=imgSize.width*ratioW;
        float diffW = button.frame.size.width - scaledw;
        if(scaledw < button.frame.size.width) {
            button.frame = CGRectMake((diffW/2)+button.frame.origin.x+5, button.frame.origin.y+5, scaledw-10, button.frame.size.height-10);
        }
        if (myAI != nil)
            [myAI removeFromSuperview];
    }
    
    if (myAI != nil) {
        [myAI removeFromSuperview];
        [myAI setHidden:YES];
        [self performSelectorInBackground:@selector(StopAI:) withObject:myAI];
    }
}

- (void) StopAI:(UIActivityIndicatorView *) myAI
{
    [myAI removeFromSuperview];
    [myAI setHidden:YES];
}

//- (void) SetImageWithSaveWithoutSizeWithURL:(NSString *) URL TO:(id) view WithDefaultImage:(NSString *) strImage InBackGround:(BOOL) boolBackGround
- (void) SetImageWithSaveWithoutSizeWithURL:(NSString *) URL TO:(id) view WithDefaultImage:(NSString *) strImage InBackGround:(BOOL) boolBackGround WithAI:(UIActivityIndicatorView *) myAI
{
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    UIImage * image = [UIImage imageWithData:[defaults objectForKey:URL]];
    
    if (image == nil) {
        NSURLRequest * request = [NSURLRequest requestWithURL:[NSURL URLWithString:URL]];
        [NSURLConnection sendAsynchronousRequest:request
                                           queue:[NSOperationQueue mainQueue]
                               completionHandler:^(NSURLResponse * response, NSData * data, NSError * error)
         {
             UIImage * image;
             if (data != nil) {
                 image = [UIImage imageWithData:data];
             } else {
                 image = [UIImage imageNamed:strImage];
             }
             
             if (image != nil) {
                 NSData *imageData = UIImageJPEGRepresentation(image, 1.0);
                 [defaults setValue:imageData forKey:URL];
                 [defaults synchronize];
             }
             
             if(myAI != nil)
                 [myAI removeFromSuperview];
         }];
    } else {
        
        if ([view isKindOfClass:[UIButton class]]) {
            UIButton * btn = (UIButton *) view;
            if (boolBackGround) {
                [btn setBackgroundImage:image forState:UIControlStateNormal];
            } else {
                [btn setImage:image forState:UIControlStateNormal];
            }
        } else {
            UIImageView * img = (UIImageView *) view;
            [img setImage:image];
        }
        
        if(myAI != nil)
            [myAI removeFromSuperview];
    }
}

- (void) SetImageFromSizeWithoutSaveWithURL:(NSString *) URL TO:(id) view WithDefaultImage:(NSString *) strImage InBackGround:(BOOL) boolBackGround WithAI:(UIActivityIndicatorView *) myAI
{
    NSURLRequest * request = [NSURLRequest requestWithURL:[NSURL URLWithString:URL]];
    [NSURLConnection sendAsynchronousRequest:request
                                       queue:[NSOperationQueue mainQueue]
                           completionHandler:^(NSURLResponse * response, NSData * data, NSError * error)
     {
         UIImage * image;
         if (data != nil) {
             image = [UIImage imageWithData:data];
         } else {
             image = [UIImage imageNamed:strImage];
         }
         
         [self SetDownloadImage:view AndUIImage:image InBackground:boolBackGround];
         
         if(myAI != nil)
             [myAI removeFromSuperview];
     }];
}

- (void) SetImageFromSizeWithSaveWithURL:(NSString *) URL TO:(id) view WithDefaultImage:(NSString *) strImage InBackGround:(BOOL) boolBackGround WithAI:(UIActivityIndicatorView *) myAI
{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    UIImage * image = [UIImage imageWithData:[defaults objectForKey:URL]];
    
    if (image == nil) {
        NSURLRequest * request = [NSURLRequest requestWithURL:[NSURL URLWithString:URL]];
        [NSURLConnection sendAsynchronousRequest:request
                                           queue:[NSOperationQueue mainQueue]
                               completionHandler:^(NSURLResponse * response, NSData * data, NSError * error)
         {
             UIImage * image;
             if (data != nil) {
                 image = [UIImage imageWithData:data];
             } else {
                 image = [UIImage imageNamed:strImage];
             }
             
             if (image != nil) {
                 NSData *imageData = UIImageJPEGRepresentation(image, 1.0);
                 [defaults setValue:imageData forKey:URL];
                 [defaults synchronize];
             }
             
             [self SetDownloadImage:view AndUIImage:image InBackground:boolBackGround];
             
             if(myAI != nil)
                 [myAI removeFromSuperview];
         }];
    } else {
        [self SetDownloadImage:view AndUIImage:image InBackground:boolBackGround];
        
        if(myAI != nil)
            [myAI removeFromSuperview];
    }
}

- (void) SetDownloadImage:(id) view AndUIImage:(UIImage *) image InBackground:(BOOL) boolBackGround
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
    //NSLog(@"1Harish-%f-%f", Wv, Hv);
    double Wi = sizeImage.width;
    double Hi = sizeImage.height;
    //NSLog(@"2Harish-%f-%f", Wi, Hi);
    int w = 1;
    int h = 2;
    
    int Vn = (Wv>Hv)?h:w;
    //NSLog(@"3Harish-%d", Vn);
    if (Vn == h) {
        if (Hv < Hi) {
            float Nh = Hi-Hv;
            float Hp = (Hv/Hi)*100;
            float Nw = (Wi*Hp)/100;
            //NSLog(@"4Harish-%f-%f-%f", Nh, Nw, Hp);
            image = [self imageWithImage:image scaledToSizeWithSameAspectRatio:CGSizeMake(Nw, Hv)];//[self getImageWithSize:image withWidth:Nw andWithHeight:Hv];
            //[self getImageWithSize:image withWidth:Nw andWithHeight:Hv];
        }
    } else {
        if (Wv < Wi) {
            float Nw = Wi-Wv;
            float Wp = (Wv/Wi)*100;
            float Nh = (Hi*Wp)/100;
            //NSLog(@"6Harish-%f-%f-%f", Nw, Nh, Wp);
            image = image = [self imageWithImage:image scaledToSizeWithSameAspectRatio:CGSizeMake(Wv, Nh)];//[self getImageWithSize:image withWidth:Wv andWithHeight:Nh];
            //[self getImageWithSize:image withWidth:Wv andWithHeight:Nh];
        }
    }
    
    if ([view isKindOfClass:[UIButton class]]) {
        UIButton * btn = (UIButton *) view;
        if (boolBackGround) {
            [btn setBackgroundImage:image forState:UIControlStateNormal];
        } else {
            [btn setImage:image forState:UIControlStateNormal];
        }
    } else {
        UIImageView * img = (UIImageView *) view;
        [img setImage:image];
    }
    //NSLog(@"========================================================");
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

-  (UIImage*)imageWithImage:(UIImage*)sourceImage scaledToSizeWithSameAspectRatio:(CGSize)targetSize;
{
    CGSize imageSize = sourceImage.size;
    CGFloat width = imageSize.width;
    CGFloat height = imageSize.height;
    CGFloat targetWidth = targetSize.width;
    CGFloat targetHeight = targetSize.height;
    CGFloat scaleFactor = 0.0;
    CGFloat scaledWidth = targetWidth;
    CGFloat scaledHeight = targetHeight;
    CGPoint thumbnailPoint = CGPointMake(0.0,0.0);
	
    if (CGSizeEqualToSize(imageSize, targetSize) == NO)
    {
        CGFloat widthFactor = targetWidth / width;
        CGFloat heightFactor = targetHeight / height;
		
        if (widthFactor > heightFactor) {
            scaleFactor = widthFactor; // scale to fit height
        } else {
            scaleFactor = heightFactor; // scale to fit width
        }
		
        scaledWidth  = width * scaleFactor;
        scaledHeight = height * scaleFactor;
		
        // center the image
        if (widthFactor > heightFactor) {
            thumbnailPoint.y = (targetHeight - scaledHeight) * 0.5;
        } else if (widthFactor < heightFactor) {
            thumbnailPoint.x = (targetWidth - scaledWidth) * 0.5;
        }
    }
	
    CGImageRef imageRef = [sourceImage CGImage];
    CGBitmapInfo bitmapInfo = CGImageGetBitmapInfo(imageRef);
    CGColorSpaceRef colorSpaceInfo = CGImageGetColorSpace(imageRef);
	
    if (bitmapInfo == kCGImageAlphaNone)
    {
        bitmapInfo = kCGImageAlphaNoneSkipLast;
    }
	
    CGContextRef bitmap;
	
    if (sourceImage.imageOrientation == UIImageOrientationUp || sourceImage.imageOrientation == UIImageOrientationDown)
    {
        bitmap = CGBitmapContextCreate(NULL, targetWidth, targetHeight, CGImageGetBitsPerComponent(imageRef), CGImageGetBytesPerRow(imageRef), colorSpaceInfo, bitmapInfo);
    }
    else
    {
        bitmap = CGBitmapContextCreate(NULL, targetHeight, targetWidth, CGImageGetBitsPerComponent(imageRef), CGImageGetBytesPerRow(imageRef), colorSpaceInfo, bitmapInfo);
    }
	
    // In the right or left cases, we need to switch scaledWidth and scaledHeight,
    // and also the thumbnail point
    if (sourceImage.imageOrientation == UIImageOrientationLeft)
    {
        thumbnailPoint = CGPointMake(thumbnailPoint.y, thumbnailPoint.x);
        CGFloat oldScaledWidth = scaledWidth;
        scaledWidth = scaledHeight;
        scaledHeight = oldScaledWidth;
		
        CGContextRotateCTM (bitmap, 90 * (3.1415927/180.0));
        CGContextTranslateCTM (bitmap, 0, -targetHeight);
		
    }
    else if (sourceImage.imageOrientation == UIImageOrientationRight)
    {
        thumbnailPoint = CGPointMake(thumbnailPoint.y, thumbnailPoint.x);
        CGFloat oldScaledWidth = scaledWidth;
        scaledWidth = scaledHeight;
        scaledHeight = oldScaledWidth;
		
        CGContextRotateCTM (bitmap, -90 * (3.1415927/180.0));
        CGContextTranslateCTM (bitmap, -targetWidth, 0);
		
    }
    else if (sourceImage.imageOrientation == UIImageOrientationUp)
    {
        // NOTHING
    }
    else if (sourceImage.imageOrientation == UIImageOrientationDown)
    {
        CGContextTranslateCTM (bitmap, targetWidth, targetHeight);
        CGContextRotateCTM (bitmap, -180. * (3.1415927/180.0));
    }
	
    CGContextDrawImage(bitmap, CGRectMake(thumbnailPoint.x, thumbnailPoint.y, scaledWidth, scaledHeight), imageRef);
    CGImageRef ref = CGBitmapContextCreateImage(bitmap);
    UIImage* newImage = [UIImage imageWithCGImage:ref];
	
    CGContextRelease(bitmap);
    CGImageRelease(ref);
	
    return newImage;
}

- (void) GetDurationWithCurrentLat:(float) cLat CurrentLong:(float) cLong AndPickupLat:(float) pLat PickupLong:(float) pLong ToLabel:(UILabel *) lbl WithExtraText:(NSString *) extra DistToLabel:(UILabel *) lblDist
{
    NSString * url = [NSString stringWithFormat:@"http://maps.googleapis.com/maps/api/distancematrix/json?origins=%f,%f&destinations=%f,%f&mode=driving&sensor=false", cLat, cLong, pLat, pLong];
    
    //NSLog(@"setLocation-%@",url);

}

@end
