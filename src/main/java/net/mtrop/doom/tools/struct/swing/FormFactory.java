/*******************************************************************************
 * Copyright (c) 2019-2021 Black Rook Software
 * This program and the accompanying materials are made available under 
 * the terms of the MIT License, which accompanies this distribution.
 ******************************************************************************/
package net.mtrop.doom.tools.struct.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * A field factory that creates form fields.
 * @author Matthew Tropiano
 */
public final class FormFactory
{
	private FormFactory() {}
	
	/**
	 * Creates a new text field that stores a custom value type, with a "browse" 
	 * button that calls an outside function to set a value.
	 * @param <T> the type that this field stores.
	 * @param initialValue the field's initial value.
	 * @param browseText the text to put in the browse button.
	 * @param browseFunction the function to call when the browse button is clicked (the parameter is the current value).
	 * @param converter the converter interface for text to value and back.
	 * @param changeListener the listener to use when a value change occurs.
	 * @return the generated field.
	 */
	public static <T> JFormField<T> valueBrowseTextField(T initialValue, String browseText, Function<T, T> browseFunction, JValueConverter<T> converter, JValueChangeListener<T> changeListener)
	{
		return new JValueBrowseField<>(initialValue, browseText, browseFunction, converter, changeListener);
	}

	/**
	 * Creates a new text field that stores a custom value type, with a "browse" 
	 * button that calls an outside function to set a value.
	 * @param <T> the type that this field stores.
	 * @param initialValue the field's initial value.
	 * @param browseFunction the function to call when the browse button is clicked (the parameter is the current value).
	 * @param converter the converter interface for text to value and back.
	 * @param changeListener the listener to use when a value change occurs.
	 * @return the generated field.
	 */
	public static <T> JFormField<T> valueBrowseTextField(T initialValue, Function<T, T> browseFunction, JValueConverter<T> converter, JValueChangeListener<T> changeListener)
	{
		return valueBrowseTextField(initialValue, "...", browseFunction, converter, changeListener);
	}

	/**
	 * Creates a new text field that stores a custom value type, with a "browse" 
	 * button that calls an outside function to set a value.
	 * @param <T> the type that this field stores.
	 * @param initialValue the field's initial value.
	 * @param browseText the text to put in the browse button.
	 * @param browseFunction the function to call when the browse button is clicked (the parameter is the current value).
	 * @param converter the converter interface for text to value and back.
	 * @return the generated field.
	 */
	public static <T> JFormField<T> valueBrowseTextField(T initialValue, String browseText, Function<T, T> browseFunction, JValueConverter<T> converter)
	{
		return valueBrowseTextField(initialValue, browseText, browseFunction, converter, null);
	}

	/**
	 * Creates a new text field that stores a custom value type, with a "browse" 
	 * button that calls an outside function to set a value.
	 * @param <T> the type that this field stores.
	 * @param initialValue the field's initial value.
	 * @param browseFunction the function to call when the browse button is clicked (the parameter is the current value).
	 * @param converter the converter interface for text to value and back.
	 * @return the generated field.
	 */
	public static <T> JFormField<T> valueBrowseTextField(T initialValue, Function<T, T> browseFunction, JValueConverter<T> converter)
	{
		return valueBrowseTextField(initialValue, "...", browseFunction, converter, null);
	}

	/**
	 * Creates a new text field that stores a custom value type, with a "browse" 
	 * button that calls an outside function to set a value.
	 * @param <T> the type that this field stores.
	 * @param browseText the text to put in the browse button.
	 * @param browseFunction the function to call when the browse button is clicked (the parameter is the current value).
	 * @param converter the converter interface for text to value and back.
	 * @return the generated field.
	 */
	public static <T> JFormField<T> valueBrowseTextField(String browseText, Function<T, T> browseFunction, JValueConverter<T> converter)
	{
		return valueBrowseTextField(null, browseText, browseFunction, converter, null);
	}

	/**
	 * Creates a new text field that stores a custom value type, with a "browse" 
	 * button that calls an outside function to set a value.
	 * @param <T> the type that this field stores.
	 * @param browseFunction the function to call when the browse button is clicked (the parameter is the current value).
	 * @param converter the converter interface for text to value and back.
	 * @return the generated field.
	 */
	public static <T> JFormField<T> valueBrowseTextField(Function<T, T> browseFunction, JValueConverter<T> converter)
	{
		return valueBrowseTextField(null, "...", browseFunction, converter, null);
	}

	/**
	 * Creates a new text field that stores a custom value type.
	 * @param <T> the type that this field stores.
	 * @param initialValue the field's initial value.
	 * @param converter the converter interface for text to value and back.
	 * @param changeListener the listener to use when a value change occurs.
	 * @return the generated field.
	 */
	public static <T> JFormField<T> valueTextField(T initialValue, JValueConverter<T> converter, JValueChangeListener<T> changeListener)
	{
		return new JValueTextField<>(initialValue, converter, changeListener);
	}

	/**
	 * Creates a new text field that stores a custom value type.
	 * @param <T> the type that this field stores.
	 * @param converter the converter interface for text to value and back.
	 * @param changeListener the listener to use when a value change occurs.
	 * @return the generated field.
	 */
	public static <T> JFormField<T> valueTextField(JValueConverter<T> converter, JValueChangeListener<T> changeListener)
	{
		return valueTextField(null, converter, changeListener);
	}

	/**
	 * Creates a new text field that stores a custom value type.
	 * @param <T> the type that this field stores.
	 * @param initialValue the field's initial value.
	 * @param converter the converter interface for text to value and back.
	 * @return the generated field.
	 */
	public static <T> JFormField<T> valueTextField(T initialValue, JValueConverter<T> converter)
	{
		return valueTextField(initialValue, converter, null);
	}

	/**
	 * Creates a new text field that stores a custom value type.
	 * @param <T> the type that this field stores.
	 * @param converter the converter interface for text to value and back.
	 * @return the generated field.
	 */
	public static <T> JFormField<T> valueTextField(JValueConverter<T> converter)
	{
		return valueTextField(null, converter);
	}

	/**
	 * Creates a new text field that stores a string type.
	 * Empty strings are considered null if this field is nullable.
	 * Nulls are converted to empty string.
	 * @param initialValue the field's initial value.
	 * @param nullable if true, this is a nullable field.
	 * @param changeListener the listener to use when a value change occurs.
	 * @return the generated field.
	 */
	public static JFormField<String> stringTextField(String initialValue, final boolean nullable, JValueChangeListener<String> changeListener)
	{
		return valueTextField(initialValue, converter(
			(text) -> {
				text = text.trim();
				return text.length() == 0 ? (nullable ? null : "") : text;
			},
			(value) -> (
				value != null ? String.valueOf(value) : ""
			)
		), changeListener); 
	}

	/**
	 * Creates a new text field that stores a string type.
	 * @param initialValue the field's initial value.
	 * @param changeListener the listener to use when a value change occurs.
	 * @return the generated field.
	 */
	public static JFormField<String> stringTextField(String initialValue, JValueChangeListener<String> changeListener)
	{
		return stringTextField(initialValue, false, changeListener); 
	}

	/**
	 * Creates a new text field that stores a string type.
	 * Empty strings are considered null if this field is nullable.
	 * Nulls are converted to empty string.
	 * @param initialValue the field's initial value.
	 * @param nullable if true, this is a nullable field.
	 * @return the generated field.
	 */
	public static JFormField<String> stringTextField(String initialValue, final boolean nullable)
	{
		return stringTextField(initialValue, false, null); 
	}

	/**
	 * Creates a new text field that stores a string type.
	 * @param initialValue the field's initial value.
	 * @return the generated field.
	 */
	public static JFormField<String> stringTextField(String initialValue)
	{
		return stringTextField(initialValue, false, null); 
	}

	/**
	 * Creates a new text field that stores an double type.
	 * A blank value means null.
	 * @param initialValue the field's initial value.
	 * @param nullable if true, this is a nullable field.
	 * @param changeListener the listener to use when a value change occurs.
	 * @return the generated field.
	 */
	public static JFormField<Double> doubleTextField(Double initialValue, final boolean nullable, JValueChangeListener<Double> changeListener)
	{
		return valueTextField(initialValue, converter(
			(text) -> {
				try {
					return Double.parseDouble(text.trim());
				} catch (NumberFormatException e) {
					return nullable ? null : 0.0;
				}
			},
			(value) -> (
				value != null ? String.valueOf(value) : (nullable ? "" : "0.0")
			)
		), changeListener); 
	}

	/**
	 * Creates a new text field that stores an double type.
	 * @param initialValue the field's initial value.
	 * @param changeListener the listener to use when a value change occurs.
	 * @return the generated field.
	 */
	public static JFormField<Double> doubleTextField(Double initialValue, JValueChangeListener<Double> changeListener)
	{
		return doubleTextField(initialValue, false, changeListener); 
	}

	/**
	 * Creates a new text field that stores an double type.
	 * A blank value means null.
	 * @param initialValue the field's initial value.
	 * @param nullable if true, this is a nullable field.
	 * @return the generated field.
	 */
	public static JFormField<Double> doubleTextField(Double initialValue, final boolean nullable)
	{
		return doubleTextField(initialValue, nullable, null); 
	}

	/**
	 * Creates a new text field that stores an double type.
	 * @param initialValue the field's initial value.
	 * @return the generated field.
	 */
	public static JFormField<Double> doubleTextField(Double initialValue)
	{
		return doubleTextField(initialValue, false, null); 
	}

	/**
	 * Creates a new text field that stores a float type.
	 * A blank value means null.
	 * @param initialValue the field's initial value.
	 * @param nullable if true, this is a nullable field.
	 * @param changeListener the listener to use when a value change occurs.
	 * @return the generated field.
	 */
	public static JFormField<Float> floatTextField(Float initialValue, final boolean nullable, JValueChangeListener<Float> changeListener)
	{
		return valueTextField(initialValue, converter(
			(text) -> {
				try {
					return Float.parseFloat(text.trim());
				} catch (NumberFormatException e) {
					return nullable ? null : 0.0f;
				}
			},
			(value) -> (
				value != null ? String.valueOf(value) : (nullable ? "" : "0")
			)
		), changeListener); 
	}

	/**
	 * Creates a new text field that stores a float type.
	 * @param initialValue the field's initial value.
	 * @param changeListener the listener to use when a value change occurs.
	 * @return the generated field.
	 */
	public static JFormField<Float> floatTextField(Float initialValue, JValueChangeListener<Float> changeListener)
	{
		return floatTextField(initialValue, false, changeListener); 
	}

	/**
	 * Creates a new text field that stores a float type.
	 * A blank value means null.
	 * @param initialValue the field's initial value.
	 * @param nullable if true, this is a nullable field.
	 * @return the generated field.
	 */
	public static JFormField<Float> floatTextField(Float initialValue, final boolean nullable)
	{
		return floatTextField(initialValue, nullable, null); 
	}

	/**
	 * Creates a new text field that stores a float type.
	 * @param initialValue the field's initial value.
	 * @return the generated field.
	 */
	public static JFormField<Float> floatTextField(Float initialValue)
	{
		return floatTextField(initialValue, false, null); 
	}

	/**
	 * Creates a new text field that stores an long integer type.
	 * A blank value means null.
	 * @param initialValue the field's initial value.
	 * @param nullable if true, this is a nullable field.
	 * @param changeListener the listener to use when a value change occurs.
	 * @return the generated field.
	 */
	public static JFormField<Long> longTextField(Long initialValue, final boolean nullable, JValueChangeListener<Long> changeListener)
	{
		return valueTextField(initialValue, converter(
			(text) -> {
				try {
					return Long.parseLong(text.trim());
				} catch (NumberFormatException e) {
					return nullable ? null : (long)0;
				}
			},
			(value) -> (
				value != null ? String.valueOf(value) : (nullable ? "" : "0")
			)
		), changeListener); 
	}

	/**
	 * Creates a new text field that stores an long integer type.
	 * @param initialValue the field's initial value.
	 * @param changeListener the listener to use when a value change occurs.
	 * @return the generated field.
	 */
	public static JFormField<Long> longTextField(Long initialValue, JValueChangeListener<Long> changeListener)
	{
		return longTextField(initialValue, false, changeListener); 
	}

	/**
	 * Creates a new text field that stores an long integer type.
	 * A blank value means null.
	 * @param initialValue the field's initial value.
	 * @param nullable if true, this is a nullable field.
	 * @return the generated field.
	 */
	public static JFormField<Long> longTextField(Long initialValue, final boolean nullable)
	{
		return longTextField(initialValue, nullable, null); 
	}

	/**
	 * Creates a new text field that stores an long integer type.
	 * @param initialValue the field's initial value.
	 * @return the generated field.
	 */
	public static JFormField<Long> longTextField(Long initialValue)
	{
		return longTextField(initialValue, false, null); 
	}

	/**
	 * Creates a new text field that stores an integer type.
	 * A blank value means null.
	 * @param initialValue the field's initial value.
	 * @param nullable if true, this is a nullable field.
	 * @param changeListener the listener to use when a value change occurs.
	 * @return the generated field.
	 */
	public static JFormField<Integer> integerTextField(Integer initialValue, final boolean nullable, JValueChangeListener<Integer> changeListener)
	{
		return valueTextField(initialValue, converter(
			(text) -> {
				try {
					return Integer.parseInt(text.trim());
				} catch (NumberFormatException e) {
					return nullable ? null : 0;
				}
			},
			(value) -> (
				value != null ? String.valueOf(value) : (nullable ? "" : "0")
			)
		), changeListener); 
	}

	/**
	 * Creates a new text field that stores an integer type.
	 * @param initialValue the field's initial value.
	 * @param changeListener the listener to use when a value change occurs.
	 * @return the generated field.
	 */
	public static JFormField<Integer> integerTextField(Integer initialValue, JValueChangeListener<Integer> changeListener)
	{
		return integerTextField(initialValue, false, changeListener);
	}

	/**
	 * Creates a new text field that stores an integer type.
	 * A blank value means null.
	 * @param initialValue the field's initial value.
	 * @param nullable if true, this is a nullable field.
	 * @return the generated field.
	 */
	public static JFormField<Integer> integerTextField(Integer initialValue, boolean nullable)
	{
		return integerTextField(initialValue, nullable, null);
	}

	/**
	 * Creates a new text field that stores an integer type.
	 * @param initialValue the field's initial value.
	 * @return the generated field.
	 */
	public static JFormField<Integer> integerTextField(Integer initialValue)
	{
		return integerTextField(initialValue, false, null);
	}

	/**
	 * Creates a new text field that stores a short type.
	 * A blank value means null.
	 * @param initialValue the field's initial value.
	 * @param nullable if true, this is a nullable field.
	 * @param changeListener the listener to use when a value change occurs.
	 * @return the generated field.
	 */
	public static JFormField<Short> shortTextField(Short initialValue, final boolean nullable, JValueChangeListener<Short> changeListener)
	{
		return valueTextField(initialValue, converter(
			(text) -> {
				try {
					return Short.parseShort(text.trim());
				} catch (NumberFormatException e) {
					return nullable ? null : (short)0;
				}
			},
			(value) -> (
				value != null ? String.valueOf(value) : (nullable ? "" : "0")
			)
		), changeListener); 
	}

	/**
	 * Creates a new text field that stores a short type.
	 * @param initialValue the field's initial value.
	 * @param changeListener the listener to use when a value change occurs.
	 * @return the generated field.
	 */
	public static JFormField<Short> shortTextField(Short initialValue, JValueChangeListener<Short> changeListener)
	{
		return shortTextField(initialValue, false, changeListener); 
	}

	/**
	 * Creates a new text field that stores a short type.
	 * A blank value means null.
	 * @param initialValue the field's initial value.
	 * @param nullable if true, this is a nullable field.
	 * @return the generated field.
	 */
	public static JFormField<Short> shortTextField(Short initialValue, boolean nullable)
	{
		return shortTextField(initialValue, nullable, null); 
	}

	/**
	 * Creates a new text field that stores a short type.
	 * @param initialValue the field's initial value.
	 * @return the generated field.
	 */
	public static JFormField<Short> shortTextField(Short initialValue)
	{
		return shortTextField(initialValue, false, null); 
	}

	/**
	 * Creates a new text field that stores a byte type.
	 * A blank value means null.
	 * @param initialValue the field's initial value.
	 * @param nullable if true, this is a nullable field.
	 * @param changeListener the listener to use when a value change occurs.
	 * @return the generated field.
	 */
	public static JFormField<Byte> byteTextField(Byte initialValue, final boolean nullable, JValueChangeListener<Byte> changeListener)
	{
		return valueTextField(initialValue, converter(
			(text) -> {
				try {
					return Byte.parseByte(text.trim());
				} catch (NumberFormatException e) {
					return nullable ? null : (byte)0;
				}
			},
			(value) -> (
				value != null ? String.valueOf(value) : (nullable ? "" : "0")
			)
		), changeListener);
	}

	/**
	 * Creates a new text field that stores a byte type.
	 * @param initialValue the field's initial value.
	 * @param changeListener the listener to use when a value change occurs.
	 * @return the generated field.
	 */
	public static JFormField<Byte> byteTextField(Byte initialValue, JValueChangeListener<Byte> changeListener)
	{
		return byteTextField(initialValue, false, changeListener); 
	}

	/**
	 * Creates a new text field that stores a byte type.
	 * A blank value means null.
	 * @param initialValue the field's initial value.
	 * @param nullable if true, this is a nullable field.
	 * @return the generated field.
	 */
	public static JFormField<Byte> byteTextField(Byte initialValue, boolean nullable)
	{
		return byteTextField(initialValue, nullable, null); 
	}

	/**
	 * Creates a new text field that stores a byte type.
	 * @param initialValue the field's initial value.
	 * @return the generated field.
	 */
	public static JFormField<Byte> byteTextField(Byte initialValue)
	{
		return byteTextField(initialValue, false, null); 
	}

	/* ==================================================================== */

	/**
	 * Creates a new file field with a button to browse for a file.
	 * @param initialValue the field's initial value.
	 * @param browseText the browse button text.
	 * @param browseFunction the function to call to browse for a file.
	 * @param changeListener the listener to use when a value change occurs.
	 * @return the new file selection field.
	 */
	public static JFormField<File> fileField(File initialValue, String browseText, Function<File, File> browseFunction, JValueChangeListener<File> changeListener)
	{
		return valueBrowseTextField(initialValue, browseText, browseFunction, 
			converter(
				(text) -> {
					text = text.trim();
					if (text.length() == 0)
						return null;
					return new File(text);
				},
				(value) -> value != null ? (value.exists() ? value.getAbsolutePath() : value.getPath()) : ""
			), 
			changeListener
		);
	}
	
	/**
	 * Creates a new file field with a button to browse for a file.
	 * @param initialValue the field's initial value.
	 * @param browseFunction the function to call to browse for a file.
	 * @param changeListener the listener to use when a value change occurs.
	 * @return the new file selection field.
	 */
	public static JFormField<File> fileField(File initialValue, Function<File, File> browseFunction, JValueChangeListener<File> changeListener)
	{
		return fileField(initialValue, "...", browseFunction, changeListener);
	}
	
	/**
	 * Creates a new file field with a button to browse for a file.
	 * @param browseFunction the function to call to browse for a file.
	 * @param changeListener the listener to use when a value change occurs.
	 * @return the new file selection field.
	 */
	public static JFormField<File> fileField(Function<File, File> browseFunction, JValueChangeListener<File> changeListener)
	{
		return fileField(null, "...", browseFunction, changeListener);
	}
	
	/**
	 * Creates a new file field with a button to browse for a file.
	 * @param initialValue the field's initial value.
	 * @param browseText the browse button text.
	 * @param browseFunction the function to call to browse for a file.
	 * @return the new file selection field.
	 */
	public static JFormField<File> fileField(File initialValue, String browseText, Function<File, File> browseFunction)
	{
		return fileField(initialValue, browseText, browseFunction, null);
	}
	
	/**
	 * Creates a new file field with a button to browse for a file.
	 * @param browseText the browse button text.
	 * @param browseFunction the function to call to browse for a file.
	 * @return the new file selection field.
	 */
	public static JFormField<File> fileField(String browseText, Function<File, File> browseFunction)
	{
		return fileField(null, browseText, browseFunction, null);
	}
	
	/**
	 * Creates a new file field with a button to browse for a file.
	 * @param initialValue the field's initial value.
	 * @param browseFunction the function to call to browse for a file.
	 * @return the new file selection field.
	 */
	public static JFormField<File> fileField(File initialValue, Function<File, File> browseFunction)
	{
		return fileField(initialValue, "...", browseFunction, null);
	}
	
	/**
	 * Creates a new file field with a button to browse for a file.
	 * @param browseFunction the function to call to browse for a file.
	 * @return the new file selection field.
	 */
	public static JFormField<File> fileField(Function<File, File> browseFunction)
	{
		return fileField(null, "...", browseFunction, null);
	}
	
	/* ==================================================================== */

	/**
	 * Creates a form field from a text area.
	 * @param textArea the text area to encapsulate.
	 * @return a new form field that encapsulates a text area.
	 */
	public static JFormField<String> textAreaField(final JTextArea textArea)
	{
		return new JFormField<String>() 
		{
			private static final long serialVersionUID = 2756507116966376754L;
			
			private JTextArea field;
			
			{
				setLayout(new BorderLayout());
				add(BorderLayout.CENTER, this.field = textArea);
			}
			
			@Override
			public String getValue()
			{
				return field.getText();
			}

			@Override
			public void setValue(String value)
			{
				field.setText(value);
			}
		};
	}

	/**
	 * Creates a form field from a check box.
	 * @param checkBox the checkbox to encapsulate.
	 * @return a new form field that encapsulates a checkbox.
	 */
	public static JFormField<Boolean> checkBoxField(final JCheckBox checkBox)
	{
		return new JFormField<Boolean>() 
		{
			private static final long serialVersionUID = -1477632818725772731L;

			private JCheckBox field;
			
			{
				setLayout(new BorderLayout());
				add(BorderLayout.CENTER, this.field = checkBox);
			}
			
			@Override
			public Boolean getValue()
			{
				return field.isSelected();
			}

			@Override
			public void setValue(Boolean value)
			{
				field.setSelected(value);
			}
		};
	}
	
	/**
	 * Creates a form field from a slider.
	 * @param minLabel the minimum value label.
	 * @param maxLabel the maximum value label.
	 * @param slider the slider to encapsulate.
	 * @return a new form field that encapsulates a slider.
	 */
	public static JFormField<Integer> sliderField(String minLabel, String maxLabel, final JSlider slider)
	{
		return new JFormField<Integer>() 
		{
			private static final long serialVersionUID = 4363610257614419998L;

			private JSlider field;
			
			{
				setLayout(new BorderLayout());
				if (minLabel != null)
					add(BorderLayout.WEST, new JLabel(minLabel));
				add(BorderLayout.CENTER, this.field = slider);
				if (maxLabel != null)
					add(BorderLayout.EAST, new JLabel(maxLabel));
			}
			
			@Override
			public Integer getValue()
			{
				return field.getValue();
			}

			@Override
			public void setValue(Integer value)
			{
				field.setValue(value);
			}
		};
	}
	
	/**
	 * Creates a form field from a slider.
	 * @param slider the slider to encapsulate.
	 * @return a new form field that encapsulates a slider.
	 */
	public static JFormField<Integer> sliderField(final JSlider slider)
	{
		return sliderField(
			String.valueOf(slider.getModel().getMinimum()),
			String.valueOf(slider.getModel().getMaximum()),
			slider
		);
	}

	/**
	 * Creates a form field from a spinner.
	 * @param <T> the spinner return type.
	 * @param spinner the spinner to encapsulate.
	 * @return a new form field that encapsulates a spinner.
	 */
	public static <T> JFormField<T> spinnerField(final JSpinner spinner)
	{
		return new JFormField<T>() 
		{
			private static final long serialVersionUID = -876324303202896183L;
			
			private JSpinner field;
			
			{
				setLayout(new BorderLayout());
				add(BorderLayout.CENTER, this.field = spinner);
			}
			
			@Override
			@SuppressWarnings("unchecked")
			public T getValue()
			{
				return (T)field.getValue();
			}

			@Override
			public void setValue(Object value)
			{
				field.setValue(value);
			}
		};
	}
	
	/**
	 * Creates a form field from a combo box.
	 * @param <T> the combo box type.
	 * @param comboBox the combo box to encapsulate.
	 * @return a new form field that encapsulates a combo box.
	 */
	public static <T> JFormField<T> comboField(final JComboBox<T> comboBox)
	{
		return new JFormField<T>() 
		{
			private static final long serialVersionUID = -7563041869993609681L;

			private JComboBox<T> field;
			
			{
				setLayout(new BorderLayout());
				add(BorderLayout.CENTER, this.field = comboBox);
			}
			
			@Override
			@SuppressWarnings("unchecked")
			public T getValue()
			{
				return (T)field.getSelectedItem();
			}

			@Override
			public void setValue(Object value)
			{
				field.setSelectedItem(value);
			}
		};
	}
	
	/**
	 * Creates a form field from a list.
	 * @param <T> the list type.
	 * @param list the list to encapsulate.
	 * @return a new form field that encapsulates a list.
	 */
	public static <T> JFormField<T> listField(final JList<T> list)
	{
		return new JFormField<T>() 
		{
			private static final long serialVersionUID = 1064013371765783373L;
			
			private JList<T> field;
			
			{
				setLayout(new BorderLayout());
				add(BorderLayout.CENTER, this.field = list);
			}
			
			@Override
			public T getValue()
			{
				return field.getSelectedValue();
			}

			@Override
			public void setValue(Object value)
			{
				field.setSelectedValue(value, true);
			}
		};
	}
	
	/* ==================================================================== */
	
	/**
	 * Creates a form panel.
	 * @param labelSide the label side.
	 * @param labelJustification the label justification.
	 * @param labelWidth the label width.
	 * @return a new form panel.
	 */
	public static JFormPanel form(JFormPanel.LabelSide labelSide, JFormPanel.LabelJustification labelJustification, int labelWidth)
	{
		return new JFormPanel(labelSide, labelJustification, labelWidth);
	}
	
	/* ==================================================================== */

	/**
	 * Creates a value converter component for text fields that use a string represent a value.
	 * @param <T> the final field type.
	 * @param valueFromTextFunction the function called to convert to the value from the text input.
	 * @param textFromValueFunction the function called to convert to text from the value input.
	 * @return a value converter to use with a form text field.
	 */
	public static <T> JValueConverter<T> converter(final Function<String, T> valueFromTextFunction, final Function<T, String> textFromValueFunction)
	{
		return new JValueConverter<T>() 
		{
			@Override
			public T getValueFromText(String text) 
			{
				return valueFromTextFunction.apply(text);
			}

			@Override
			public String getTextFromValue(T value) 
			{
				return textFromValueFunction.apply(value);
			}
		};
	}
	
	/* ==================================================================== */

	/**
	 * A single form.
	 */
	public static class JFormPanel extends JPanel
	{
		private static final long serialVersionUID = -3154883143018532725L;
		
		/** 
		 * Parameter for what side the label is on in the form. 
		 */
		public enum LabelSide
		{
			LEFT,
			RIGHT,
			LEADING,
			TRAILING;
		}

		/** 
		 * Parameter for the label justification.
		 */
		public enum LabelJustification
		{
			LEFT(SwingConstants.LEFT),
			CENTER(SwingConstants.CENTER),
			RIGHT(SwingConstants.RIGHT),
			LEADING(SwingConstants.LEADING),
			TRAILING(SwingConstants.TRAILING);
			
			private int alignment;
			
			private LabelJustification(int alignment)
			{
				this.alignment = alignment;
			}
			
		}

		private LabelSide labelSide;
		private LabelJustification labelJustification;
		private int labelWidth;
		private Map<Object, JFormField<?>> fieldValueMap;
		
		private JFormPanel(LabelSide labelSide, LabelJustification labelJustification, int labelWidth)
		{
			this.labelSide = labelSide;
			this.labelJustification = labelJustification;
			this.labelWidth = labelWidth;
			this.fieldValueMap = new HashMap<>();
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		}

		/**
		 * Adds a field to this form panel.
		 * @param <V> the field value type.
		 * @param labelText the form label text.
		 * @param field the field to set for the form.
		 * @return this panel.
		 */
		public <V> JFormPanel addField(String labelText, JFormField<V> field)
		{
			return addField(null, labelText, field);
		}
		
		/**
		 * Adds a field to this form panel.
		 * @param <V> the field value type.
		 * @param key the the object key to fetch/set values with (if not null).
		 * @param labelText the form label text.
		 * @param field the field to set for the form.
		 * @return this panel.
		 */
		public <V> JFormPanel addField(Object key, String labelText, JFormField<V> field)
		{
			JFormFieldPanel<V> panel;
			JLabel label = new JLabel(labelText);
			label.setHorizontalAlignment(labelJustification.alignment);
			label.setVerticalAlignment(JLabel.CENTER);
			label.setPreferredSize(new Dimension(labelWidth, 0));
			
			switch (labelSide)
			{
				default:
				case LEFT:
					panel = new JFormFieldPanel<>(label, field);
					break;
				case RIGHT:
					panel = new JFormFieldPanel<>(field, label);
					break;
			}
			if (key != null)
				fieldValueMap.put(key, panel);
			add(panel);
			return this;
		}
		
		/**
		 * Gets a form value by an associated key.
		 * @param key the key to use.
		 * @return the form field value (can be null), or null if it doesn't exist.
		 */
		public Object getValue(Object key)
		{
			JFormField<?> field = fieldValueMap.get(key);
			return field == null ? null : field.getValue();
		}
		
		/**
		 * Sets a form value by an associated key.
		 * If the key does not correspond to a value, this does nothing.
		 * @param <V> the value type.
		 * @param key the key to use.
		 * @param value the value to set.
		 */
		public <V> void setValue(Object key, V value)
		{
			JFormField<?> field;
			if ((field = fieldValueMap.get(key)) != null)
			{
				Method m;
				try {
					m = field.getClass().getMethod("setValue", value.getClass());
					m.invoke(field, value);
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new ClassCastException("Could not set form field: " + e.getLocalizedMessage());
				}
			}
		}
		
		/**
		 * Gets a form value by an associated key, cast to a specific type.
		 * @param <T> the return type. 
		 * @param type the class type to cast to.
		 * @param key the key to use.
		 * @return the form field value (can be null), or null if it doesn't exist.
		 */
		public <T> T getValue(Class<T> type, Object key)
		{
			return type.cast(getValue(key));
		}
		
	}
	
	/**
	 * Input field interface used for the Black Rook Swing input components.
	 * @param <V> the type of value stored by this field.
	 */
	public static abstract class JFormField<V> extends JPanel
	{
		private static final long serialVersionUID = 1207550884473493069L;
		
		/**
		 * @return the field's value. 
		 */
		public abstract V getValue();
		
		/**
		 * Sets the field's value.
		 * @param value the new value. 
		 */
		public abstract void setValue(V value);
		
	}

	/**
	 * An encapsulated form field with a label. 
	 * @param <T> the value type stored by this panel.
	 */
	public static class JFormFieldPanel<T> extends JFormField<T>
	{
		private static final long serialVersionUID = -7165231538037948972L;
		
		private JLabel label;
		private JFormField<T> formField;
		
		private JFormFieldPanel(JLabel label, JFormField<T> field)
		{
			super();
			setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
			setLayout(new BorderLayout(4, 0));
			add(this.label = label, BorderLayout.WEST);
			add(this.formField = field, BorderLayout.CENTER);
		}
		
		private JFormFieldPanel(JFormField<T> field, JLabel label)
		{
			super();
			setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			setLayout(new BorderLayout());
			add(this.formField = field, BorderLayout.CENTER);
			add(this.label = label, BorderLayout.EAST);
		}
		
		/**
		 * Sets the label text.
		 * @param text the new text.
		 */
		public void setLabel(String text)
		{
			label.setText(text);
		}
		
		/**
		 * @return the label component.
		 */
		public JLabel getLabel() 
		{
			return label;
		}
		
		@Override
		public T getValue()
		{
			return formField.getValue();
		}
	
		@Override
		public void setValue(T value) 
		{
			formField.setValue(value);
		}
		
	}

	/**
	 * A field with a button for "browsing" for a value to set.
	 * @param <T> the value type.
	 */
	public static class JValueBrowseField<T> extends JValueTextField<T>
	{
		private static final long serialVersionUID = 7171922756771225976L;
		
		/**
		 * Creates a new browse field.
		 * @param initialValue the initial value.
		 * @param browseText the browse button text.
		 * @param browseFunction the browse value function. 
		 * @param converter the converter for the text field value.
		 * @param changeListener the listener to call when a value changes.
		 */
		protected JValueBrowseField(T initialValue, String browseText, final Function<T, T> browseFunction, JValueConverter<T> converter, JValueChangeListener<T> changeListener)
		{
			super(initialValue, converter, changeListener);
			add(new JButton(new AbstractAction(browseText)
			{
				private static final long serialVersionUID = -7785265067430010139L;

				@Override
				public void actionPerformed(ActionEvent e) 
				{
					T value;
					if ((value = browseFunction.apply(JValueBrowseField.this.getValue())) != null)
						setValue(value);
				}
				
			}), BorderLayout.EAST);
		}
		
	}
	
	/**
	 * A text field that is the representation of a greater value.
	 * @param <T> the type that this field stores.
	 */
	public static class JValueTextField<T> extends JFormField<T>
	{
		private static final long serialVersionUID = -8674796823012708679L;
		
		/** The stored value. */
		private Object value;
		/** The stored value. */
		private JTextField textField;
		/** The converter. */
		private JValueConverter<T> converter;
		/** The change listener. */
		private JValueChangeListener<T> changeListener;
		
		/**
		 * Creates a new text field.
		 * @param initialValue the initial value.
		 * @param converter the converter for the text field value.
		 * @param changeListener the listener to call when a value changes.
		 */
		protected JValueTextField(T initialValue, JValueConverter<T> converter, JValueChangeListener<T> changeListener)
		{
			this.converter = Objects.requireNonNull(converter);
			this.changeListener = changeListener;
	
			this.textField = new JTextField();
			this.textField.addKeyListener(new KeyAdapter() 
			{
				@Override
				public void keyPressed(KeyEvent e) 
				{
					if (e.getKeyCode() == KeyEvent.VK_ENTER)
					{
						e.getComponent().transferFocus();
					}
					else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					{
						restoreValue();
						e.getComponent().transferFocus();
					}
				}
			});
			this.textField.addFocusListener(new FocusAdapter()
			{
				@Override
				public void focusGained(FocusEvent e) 
				{
					textField.selectAll();
				}
				
				@Override
				public void focusLost(FocusEvent e) 
				{
					refreshValue();
				}
			});
			
			setLayout(new BorderLayout());
			add(BorderLayout.CENTER, this.textField);
			setValue(initialValue);
		}
		
		/**
		 * Sets the value from text.
		 * @param text the text to set.
		 */
		public void setText(String text)
		{
			setValue(converter.getValueFromText(text));
		}
	
		@Override
		@SuppressWarnings("unchecked")
		public T getValue()
		{
			return (T)value;
		}
		
		@Override
		public void setValue(T value)
		{
			this.value = value;
			textField.setText(converter.getTextFromValue((T)value));
			if (changeListener != null)
				changeListener.onChange(value);
		}
		
		// Refreshes an entered value.
		private void refreshValue()
		{
			setValue(converter.getValueFromText(textField.getText()));
		}
		
		private void restoreValue()
		{
			setValue(getValue());
		}
		
	}

	/**
	 * A listener that is called when a value changes on a 
	 * @param <T> the field value type.
	 */
	@FunctionalInterface
	public static interface JValueChangeListener<T>
	{
		/**
		 * Called when the value on the form changes.
		 * @param value the new value.
		 */
		void onChange(T value);
	}
	
	/**
	 * A common interface for fields that convert values to and from text.
	 * @param <T> the object type that this converts.
	 */
	public static interface JValueConverter<T>
	{
		/**
		 * Parses text for the value to set on this field.
		 * @param text the value to set.
		 * @return the resultant value.
		 */
		public abstract T getValueFromText(String text);

		/**
		 * Turns the value set on this field into text.
		 * @param value the value to set.
		 * @return the resultant value.
		 */
		public abstract String getTextFromValue(T value);
	}

}
