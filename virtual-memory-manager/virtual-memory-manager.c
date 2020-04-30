/**
 * Demonstration C program illustrating how to perform file I/O for vm assignment.
 *
 * Input file contains logical addresses
 * 
 * Backing Store represents the file being read from disk (the backing store.)
 *
 * We need to perform random input from the backing store using fseek() and fread()
 *
 * This program performs nothing of meaning, rather it is intended to illustrate the basics
 * of I/O for the vm assignment. Using this I/O, you will need to make the necessary adjustments
 * that implement the virtual memory manager.
 *
 * Implemented by Preston McIllece
 */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

// number of characters to read for each line from input file
#define BUFFER_SIZE         10

// number of bytes to read
#define CHUNK               256
#define NUMBER_OF_FRAMES    128
#define NUMBER_OF_PAGES     256
#define SIZE_OF_TLB         16

FILE    *address_file;
FILE    *backing_store;

// how we store reads from input file
char    address[BUFFER_SIZE];

int     logical_address;
int 	physical_address;

// the buffer containing reads from backing store
signed char     physical_memory[NUMBER_OF_FRAMES][CHUNK];
int	page_table[NUMBER_OF_PAGES];
int	tlb[SIZE_OF_TLB][2];

// the value of the byte (signed char) in memory
signed char     value;

int main(int argc, char *argv[])
{
int offset_mask = 255;
int page_num_mask = 65280;
int offset = 0;
int page_number = 0;
int references = 0;
int tlb_hits = 0;
int page_faults = 0;
int frame_number = 0;
int memory_tracker = 0;
int tlb_tracker = 0;
int is_in_tlb = 0;
int page_index_in_tlb = 0;

    // perform basic error checking
    if (argc != 2) {
        fprintf(stderr,"Usage: ./hw5 [input file]\n");
        return -1;
    }

    // open the file containing the backing store
    backing_store = fopen("BACKING_STORE.bin", "rb");

    // open the file containing the logical addresses
    address_file = fopen(argv[1], "r");

    if (address_file == NULL) {
        fprintf(stderr, "Error opening %s\n",argv[1]);
        return -1;
    }

    //initialize tlb and page table entries
    for (int i = 0; i < NUMBER_OF_PAGES; ++i) {
	page_table[i] = -1;
    }
    for (int j = 0; j < SIZE_OF_TLB; ++j) {
	for (int k = 0; k < 2; ++k) {
		tlb[j][k] = -1;
	}
    }	
    // read through the input file and output each logical address
    while ( fgets(address, BUFFER_SIZE, address_file) != NULL) {
	//bit masking
        logical_address = atoi(address);
	page_number = logical_address & page_num_mask;
	page_number = page_number >> 8;
	offset = logical_address & offset_mask;

	is_in_tlb = 0; //boolean
	for (int l = 0; l < SIZE_OF_TLB; ++l) {
		if (tlb[l][0] == page_number) {
			is_in_tlb = 1;
			page_index_in_tlb = l;
		}
	}
	if (is_in_tlb) {  //tlb hit!
		frame_number = tlb[page_index_in_tlb][1];
		++tlb_hits;
	}
        else if (page_table[page_number] != -1) { //page is in the page table
		frame_number = page_table[page_number];
	}
	else { //page fault
		++page_faults;
		frame_number = memory_tracker;
		memory_tracker = (memory_tracker + 1) % NUMBER_OF_FRAMES; //implements FIFO page replacement

		//check if the assigned page frame already exists in the TLB and if so, clear that entry
		for (int entry = 0; entry < SIZE_OF_TLB; ++entry) {
			if (tlb[entry][1] == frame_number) {
				tlb[entry][0] = -1;
				tlb[entry][1] = -1;
			} 
		}	
		//check if the assigned page fram already exists in the page table and if so, clear that entry
		for (int p = 0; p < NUMBER_OF_PAGES; ++p) {
			if (page_table[p] == frame_number) 
				page_table[p] = -1; 
		}
		tlb[tlb_tracker][0] = page_number;
		tlb[tlb_tracker][1] = frame_number;
		tlb_tracker = (tlb_tracker + 1) % SIZE_OF_TLB;
		
		// first seek to byte CHUNK in the backing store
        	// SEEK_SET in fseek() seeks from the beginning of the file
        	if (fseek(backing_store, CHUNK * page_number, SEEK_SET) != 0) {
            		fprintf(stderr, "Error seeking in backing store\n"); 
	   		return -1;
        	}
		// now read CHUNK bytes from the backing store to the buffer
        	if (fread(&physical_memory[frame_number], sizeof(signed char), CHUNK, backing_store) == 0) {
            		fprintf(stderr, "Error reading from backing store\n");
            		return -1;
      		}
		page_table[page_number] = frame_number; //update the page table
	}
	value = physical_memory[frame_number][offset];
	
	//reverse masking to calculate physical address from frame number and offset
	frame_number = frame_number << 8;
	physical_address = frame_number | offset;
	
	printf("Virtual address: %d Physical address: %d Value: %d\n", logical_address, physical_address, value);    	
   	++references;
    }
    float tlb_hit_rate = tlb_hits/(float)references;
    float page_fault_rate = page_faults/(float)references;
    printf("Number of Translated Addresses = %d\n", references);
    printf("Page Faults = %d\n", page_faults);
    printf("Page Fault Rate = %f\n", page_fault_rate);
    printf("TLB Hits = %d\n", tlb_hits);
    printf("TLB Hit Rate = %f\n", tlb_hit_rate);
    fclose(address_file);
    fclose(backing_store);

    return 0;
}


