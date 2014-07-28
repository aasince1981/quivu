package com.it.reloved.lazyloader;

import android.widget.ImageView;
import android.widget.ProgressBar;

public class ProgressBarAndImage {

	public ProgressBarAndImage(String imagePath, ImageView imageView,
			ProgressBar progressBar, int cornerPixels, int borderPixels,
			int borderColor, int defaultImage) {
		super();
		this.imageView = imageView;
		this.progressBar = progressBar;
		this.imagePath = imagePath;
		this.cornerPixels = cornerPixels;
		this.borderPixels = borderPixels;
		this.borderColor = borderColor;
		this.defaultImage = defaultImage;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getDefaultImage() {
		return defaultImage;
	}

	public void setDefaultImage(int defaultImage) {
		this.defaultImage = defaultImage;
	}

	private ImageView imageView;
	private ProgressBar progressBar;
	private String imagePath;
	int cornerPixels, borderPixels, borderColor, defaultImage;

	public int getCornerPixels() {
		return cornerPixels;
	}

	public void setCornerPixels(int cornerPixels) {
		this.cornerPixels = cornerPixels;
	}

	public int getBorderPixels() {
		return borderPixels;
	}

	public void setBorderPixels(int borderPixels) {
		this.borderPixels = borderPixels;
	}

	public int getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(int borderColor) {
		this.borderColor = borderColor;
	}
}
