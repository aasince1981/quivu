//
//  CameraTest.m
//  myFotocloset
//
//  Created by Ideal IT Techno on 19/04/14.
//  Copyright (c) 2014 Ideal It Technology. All rights reserved.
//

#import "CameraTest.h"
#import "AddItemScreen.h"
#import "EditItemScreen.h"

@interface CameraTest ()

@end

@implementation CameraTest

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
    
    static NSInteger dir = 0;
    CameraTest * Item = [[CameraTest alloc]init];
    dir++;
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    Item.hidesBottomBarWhenPushed = YES;
    
    lastZoomScal = 0.0;
    UIGestureRecognizer *recognizer;
    [self.navigationController setNavigationBarHidden:YES];
    recognizer = [[UIPinchGestureRecognizer alloc] initWithTarget:self action:@selector(handlePinchFrom:)];
    recognizer.delegate = self;
    [self.view addGestureRecognizer:recognizer];
    
    // [btniPhone5LeftRight setHidden:YES];
    //[btniPhone5TopDown setHidden:YES];
    
    //    if (isIPhone)
    //        cameraEdit = [[CameraEdit alloc] initWithNibName:@"CameraEdit" bundle:nil];
    //    else
    //        cameraEdit = [[CameraEdit alloc] initWithNibName:@"CameraEdit_iPad" bundle:nil];
    //
    [[UIDevice currentDevice] beginGeneratingDeviceOrientationNotifications];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(orientationChanged:) name:UIDeviceOrientationDidChangeNotification object:[UIDevice currentDevice]];
}

- (void)viewWillAppear:(BOOL)animated
{
    KAppDelegate.MyVC = self;
    boolRotateScreen = NO;
    
    boolFrontCamera = NO;
    
    if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera]) {
        imagePickerController = [[UIImagePickerController alloc] init];
        imagePickerController.sourceType = UIImagePickerControllerSourceTypeCamera;
        
        imagePickerController.showsCameraControls = NO;
        imagePickerController.delegate = self;
        imagePickerController.toolbarHidden = YES;
        
        [self.view addSubview:imagePickerController.view];
        
        if (IS_IPHONE_5) {
            imagePickerController.view.frame = CGRectMake(0, viewUp.frame.size.height, self.view.frame.size.width, 420);
        } else {
            imagePickerController.view.frame = CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height);
        }
        
        //        UIView* overlayView = [[UIView alloc] initWithFrame:imagePickerController.view.frame];
        //        // letting png transparency be
        //        overlayView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"icon1"]];
        //        [overlayView.layer setOpaque:NO];
        //       overlayView.opaque = NO;
        //        imagePickerController.cameraOverlayView = overlayView;
        //  [imagePickerController.view addSubview:overlayView];
        [self addChildViewController:imagePickerController];
        [self.view addSubview:viewUp];
        [self.view addSubview:viewUpBottom];
    }
}

-(void)handlePinchFrom:(UIPinchGestureRecognizer*)recognizer {
    if (lastZoomScal == 0.0) {
        lastZoomScal = [recognizer scale];
    }
    
    float diff = [recognizer scale] - lastZoomScal;
    
    if (diff > 0) {
        imagePickerController.cameraViewTransform = CGAffineTransformScale(imagePickerController.cameraViewTransform, 1.01, 1.01);
    } else if (diff < 0) {
        imagePickerController.cameraViewTransform = CGAffineTransformScale(imagePickerController.cameraViewTransform, 0.99, 0.99);
    }
    
    lastZoomScal = [recognizer scale];
}

// here we call to take picture......
- (IBAction)ActionTakePicture:(id)sender
{
    if (isFlashOn) {
        //causes the flash
        UIView *flashView = [[UIView alloc] initWithFrame:[[self view] frame]];
        [flashView setBackgroundColor:[UIColor whiteColor]];
        [flashView setAlpha:0.f];
        [[[self view] window] addSubview:flashView];
        
        [UIView animateWithDuration:.5f
                         animations:^{
                             [flashView setAlpha:1.f];
                             [flashView setAlpha:0.f];
                         }
                         completion:^(BOOL finished){
                             [flashView removeFromSuperview];
                         }
         ];
        
        Class captureDeviceClass = NSClassFromString(@"AVCaptureDevice");
        if (captureDeviceClass != nil) {
            AVCaptureDevice *device = [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];
            if ([device hasTorch] && [device hasFlash]){
                [device lockForConfiguration:nil];
                [device setTorchMode:AVCaptureTorchModeOn];
                [device setFlashMode:AVCaptureFlashModeOn];
                [device unlockForConfiguration];
                
                [self performSelectorInBackground:@selector(OffTorch) withObject:nil];
            }
        }
    }
    
    [imagePickerController takePicture];
}

#pragma mark - OffTorch
- (void) OffTorch
{
    sleep(1);
    
    Class captureDeviceClass = NSClassFromString(@"AVCaptureDevice");
    if (captureDeviceClass != nil) {
        AVCaptureDevice *device = [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];
        if ([device hasTorch] && [device hasFlash]){
            [device lockForConfiguration:nil];
            [device setTorchMode:AVCaptureTorchModeOff];
            [device setFlashMode:AVCaptureFlashModeOff];
            [device unlockForConfiguration];
        }
    }
}

#pragma mark - Camera Cancel
- (IBAction)btnCancel:(id)sender {
    if (isSound) {
       // NSString *audioPath = [[NSBundle mainBundle] pathForResource:@"024627477-water-drops-4" ofType:@"mp3" ];
//        NSURL *audioURL = [NSURL fileURLWithPath:KAppDelegate.audioPath];
//        NSError *error;
//        self.audioPlayer = [[AVAudioPlayer alloc] initWithContentsOfURL:audioURL error:&error];
//        [self.audioPlayer play];
    }

//    HomeViewController *vcHomeViewController=[[HomeViewController alloc] init];
//    [self presentViewController:vcHomeViewController animated:NO completion:
//     nil
//     ];

}

-(void)dealloc {
}

- (IBAction)btnFrontCamera:(id)sender {
    if (isSound) {
       // NSString *audioPath = [[NSBundle mainBundle] pathForResource:@"024627477-water-drops-4" ofType:@"mp3" ];
//        NSURL *audioURL = [NSURL fileURLWithPath:KAppDelegate.audioPath];
//        NSError *error;
//        self.audioPlayer = [[AVAudioPlayer alloc] initWithContentsOfURL:audioURL error:&error];
//        [self.audioPlayer play];
    }

    boolFrontCamera = !boolFrontCamera;
    
    if (boolFrontCamera) {
        imagePickerController.cameraDevice = UIImagePickerControllerCameraDeviceFront;
    } else {
        imagePickerController.cameraDevice = UIImagePickerControllerCameraDeviceRear;
    }
}

#pragma mark - Image Picker Controller delegate methods
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingImage:(UIImage *) inImage editingInfo:(NSDictionary *)editingInfo {
}

-(BOOL)prefersStatusBarHidden
{
    return YES;
}

// get image from here.....
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    UIImage *image = info[UIImagePickerControllerEditedImage];

    if (!image)
        image = [info objectForKey:UIImagePickerControllerOriginalImage];
    
    if (!image)
        image = [info objectForKey:UIImagePickerControllerEditedImage];
    
    if (boolFrontCamera) {
         [self didTakePicture:image];
    } else {
        
        if(KAppDelegate.boolForEditProduct == YES) {
            if(KAppDelegate.boolImageSelect) {
                static NSInteger dir = 0;
                EditItemScreen * edit = [[EditItemScreen alloc]initWithNibName:@"EditItemScreen" bundle:nil];
                dir++;
                edit.imgProduct = image;
                KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
                
                edit.hidesBottomBarWhenPushed = YES;
                
                if(boolPickImage == YES) {
                    [self dismissViewControllerAnimated:YES completion:nil];
                }
                
                [self.navigationController pushViewController:edit animated:YES];
            } else {
                [self sendToNextClass:image];
            }
        } else {
            [self sendToNextClass:image];
            /*
            if(KAppDelegate.boolImageSelect) {
                static NSInteger dir = 0;
                AddItemScreen * add = [[AddItemScreen alloc]initWithNibName:@"AddItemScreen" bundle:nil];
                dir++;
                add.imgProduct = image;
                KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
                
                add.hidesBottomBarWhenPushed = YES;
                
                if(boolPickImage == YES) {
                    [self dismissViewControllerAnimated:YES completion:nil];
                }
                
                [self.navigationController pushViewController:add animated:YES];
                
            } else {
                [self sendToNextClass:image];
            }*/
        }
    }
}

// add effects on images..........
- (void) photoEditor:(AFPhotoEditorController *)editor finishedWithImage:(UIImage *)image
{
    [self dismissViewControllerAnimated:YES completion:^{
        
        if(KAppDelegate.boolForEditProduct == YES) {
            static NSInteger dir = 0;
            EditItemScreen * edit = [[EditItemScreen alloc]initWithNibName:@"EditItemScreen" bundle:nil];
            dir++;
            KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
            if (image) {
                KAppDelegate.imgCoverPhoto = image;
                edit.hidesBottomBarWhenPushed = YES;
                [self.navigationController pushViewController:edit animated:YES];
            } else {
                NSLog(@"image--nil");
            }            
        } else {
            static NSInteger dir = 0;
            AddItemScreen * add = [[AddItemScreen alloc]initWithNibName:@"AddItemScreen" bundle:nil];
            dir++;
            KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
            
            if(KAppDelegate.boolImageSelect) {
                add.imgProduct = image;
            } else {
                KAppDelegate.imgCoverPhoto = image;
            }
            
            add.hidesBottomBarWhenPushed = YES;
            
            [self.navigationController pushViewController:add animated:YES];
        }
    }];
}

// This is called when the user taps "Cancel" in the photo editor.
- (void) photoEditorCanceled:(AFPhotoEditorController *)editor
{
    [self dismissViewControllerAnimated:YES completion:^{
        [self.navigationController popViewControllerAnimated:NO];
    }];
}

// here we send image to addItem screen............
-(void) sendToNextClass :(UIImage *) image {
    
    if(boolPickImage == YES) {
        [self dismissViewControllerAnimated:YES completion:^{
            static dispatch_once_t onceToken;
            dispatch_once(&onceToken, ^{
                [AFPhotoEditorController setAPIKey:@"da7cee16e27a7aa0" secret:@"1e981182381d1bd9"];
            });
            AFPhotoEditorController * photoEditor = [[AFPhotoEditorController alloc] initWithImage:image];
            [photoEditor setDelegate:self];
            [self presentViewController:photoEditor animated:NO completion:^{}];
        }];
    } else {
        static dispatch_once_t onceToken;
        dispatch_once(&onceToken, ^{
            [AFPhotoEditorController setAPIKey:@"da7cee16e27a7aa0" secret:@"1e981182381d1bd9"];
        });
        AFPhotoEditorController * photoEditor = [[AFPhotoEditorController alloc] initWithImage:image];
        [photoEditor setDelegate:self];
        [self presentViewController:photoEditor animated:NO completion:^{}];
    }
}

- (void)didTakePicture:(UIImage *)picture
{
    UIImage * flippedImage = [UIImage imageWithCGImage:picture.CGImage scale:picture.scale orientation:UIImageOrientationLeftMirrored];
    picture = flippedImage;
    
    if(picture) {
        if(KAppDelegate.boolForEditProduct == YES) {
            if(KAppDelegate.boolImageSelect) {
                static NSInteger dir = 0;
                EditItemScreen * edit = [[EditItemScreen alloc]initWithNibName:@"EditItemScreen" bundle:nil];
                dir++;
                edit.imgProduct = picture;
                KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
                
                edit.hidesBottomBarWhenPushed = YES;
                
                if(boolPickImage == YES) {
                    [self dismissViewControllerAnimated:YES completion:nil];
                }
                [self.navigationController pushViewController:edit animated:YES];
            } else {
                [self sendToNextClass:picture];
            }
        } else {
            [self sendToNextClass:picture];
            /*
            if(KAppDelegate.boolImageSelect) {
                static NSInteger dir = 0;
                AddItemScreen * add = [[AddItemScreen alloc]initWithNibName:@"AddItemScreen" bundle:nil];
                dir++;
                add.imgProduct = picture;
                KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
                
                add.hidesBottomBarWhenPushed = YES;
                
                if(boolPickImage == YES) {
                    [self dismissViewControllerAnimated:YES completion:nil];
                }
                
                [self.navigationController pushViewController:add animated:YES];
                
            } else {
                [self sendToNextClass:picture];
            } */
        }
    }
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
    //[picker dismissViewControllerAnimated:NO completion:^{
        //[self.navigationController popViewControllerAnimated:YES];
   // }];
    
    [self dismissViewControllerAnimated:YES completion:nil];
    //KAppDelegate.boolPopController = NO;
    boolPickImage = NO;
}

- (IBAction)BtnProfilePicture:(id)sender {
    imagePickerController = [[UIImagePickerController alloc]init];
    imagePickerController.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
    imagePickerController.delegate = self;
    imagePickerController.allowsEditing = YES;

    [self presentModalViewController:imagePickerController animated:YES];
}

// button click on set image to gallery............
- (IBAction)btnGallery:(id)sender {
    
    imagePickerController = [[UIImagePickerController alloc]init];
    imagePickerController.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
    imagePickerController.delegate = self;
    imagePickerController.allowsEditing = YES;
    boolPickImage = YES;
    [self presentModalViewController:imagePickerController animated:YES];
}

- (void)navigationController:(UINavigationController *)navigationController willShowViewController:(UIViewController *)viewController animated:(BOOL)animated {
    [[UIApplication sharedApplication] setStatusBarHidden:YES];
}

#pragma mark - OrientationChanged Method
- (void) orientationChanged:(NSNotification *) note {
  //  [cameraEdit orientationChanged:note];
    
    @try {
        UIDevice * device = [UIDevice currentDevice];
        
        int choice = device.orientation;
        lastRotation = choice;
        if (IS_IPHONE_5) {
            switch(choice) {
                case UIDeviceOrientationPortrait:
                    [btniPhone5TopDown setImage:[UIImage imageNamed:@"rotate_icon_iphone5_Up.png"] forState:UIControlStateNormal];
                    [btniPhone5LeftRight setHidden:YES];
                    [btniPhone5TopDown setHidden:NO];
                    break;
                case UIDeviceOrientationPortraitUpsideDown:
                    [btniPhone5TopDown setImage:[UIImage imageNamed:@"rotate_icon_iphone5_up_Down.png"] forState:UIControlStateNormal];
                    [btniPhone5LeftRight setHidden:YES];
                    [btniPhone5TopDown setHidden:NO];
                    break;
                case UIDeviceOrientationLandscapeLeft:
                    [btniPhone5LeftRight setImage:[UIImage imageNamed:@"rotate_icon_iphone5_up_Right.png"] forState:UIControlStateNormal];
                    [btniPhone5TopDown setHidden:YES];
                    [btniPhone5LeftRight setHidden:NO];
                    break;
                case UIDeviceOrientationLandscapeRight:
                    [btniPhone5LeftRight setImage:[UIImage imageNamed:@"rotate_icon_iphone5_up_Left.png"] forState:UIControlStateNormal];
                    [btniPhone5TopDown setHidden:YES];
                    [btniPhone5LeftRight setHidden:NO];
                    break;
                default:
                    [btniPhone5TopDown setImage:[UIImage imageNamed:@"rotate_icon_iphone5_Up.png"] forState:UIControlStateNormal];
                    [btniPhone5LeftRight setHidden:YES];
                    [btniPhone5TopDown setHidden:NO];
                    break;
            };
        }
    } @catch (NSException *exception) { }
}

- (IBAction) btnFlash:(id)sender
{
    if (isSound) {
       // NSString *audioPath = [[NSBundle mainBundle] pathForResource:@"024627477-water-drops-4" ofType:@"mp3" ];
//        NSURL *audioURL = [NSURL fileURLWithPath:KAppDelegate.audioPath];
//        NSError *error;
//        self.audioPlayer = [[AVAudioPlayer alloc] initWithContentsOfURL:audioURL error:&error];
//        [self.audioPlayer play];
    }

    UIButton * btn = (UIButton *) sender;
    
    isFlashOn = !isFlashOn;
    
    if (isFlashOn) {
        [btn setTitle:@"" forState:UIControlStateNormal];
    } else {
        [btn setTitle:@"" forState:UIControlStateNormal];
    }
}

- (IBAction)btnCancelImage:(id)sender {
    if (isSound) {
     //   NSString *audioPath = [[NSBundle mainBundle] pathForResource:@"024627477-water-drops-4" ofType:@"mp3" ];
//        NSURL *audioURL = [NSURL fileURLWithPath:KAppDelegate.audioPath];
//        NSError *error;
//        self.audioPlayer = [[AVAudioPlayer alloc] initWithContentsOfURL:audioURL error:&error];
//        [self.audioPlayer play];
    }

    [view_images removeFromSuperview];
    [self.view addSubview:viewUpBottom];
}

#pragma mark - PushCameraEdit

- (IBAction)btnBlackShade:(id)sender {
    if (isSound) {
      //  NSString *audioPath = [[NSBundle mainBundle] pathForResource:@"024627477-water-drops-4" ofType:@"mp3" ];
//        NSURL *audioURL = [NSURL fileURLWithPath:KAppDelegate.audioPath];
//        NSError *error;
//        self.audioPlayer = [[AVAudioPlayer alloc] initWithContentsOfURL:audioURL error:&error];
//        [self.audioPlayer play];
    }
}

- (IBAction)btnCross:(id)sender {
    KAppDelegate.DictEditProductInfo = [[NSMutableDictionary alloc]init];
    KAppDelegate.dictCategoryInfo = [[NSMutableDictionary alloc]init];
    KAppDelegate.boolImageSelect = NO;
    KAppDelegate.boolForEditProduct = NO;
    
    NSLog(@"bool--> %d",KAppDelegate.boolPopController);
    if(KAppDelegate.boolPopController) {
        [self.navigationController popViewControllerAnimated:YES];
    } else {
        [KAppDelegate TabBarShow];
        KAppDelegate.dictImages = [[NSMutableDictionary alloc]init];
    }
}

- (void) viewDidUnload {
    [super viewDidUnload];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

@end
