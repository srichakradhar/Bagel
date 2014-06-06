package com.srichakradhar.bagel;

import java.util.Random;

public class Shuffler
{
int[] array = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
public  int[] random(int zero)
{
Random r = new Random();
for (int i = 0; i < (array.length - zero); i++)
{
int n = r.nextInt(array.length - zero);
// swap array[i] with array[n]
int temp = array[i];
array[i] = array[n];
array[n] = temp;
}
return array;
}}