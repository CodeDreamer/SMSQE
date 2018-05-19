	section unpack
	xdef	unpack

*
* inflate.asm
* 
* Fixed for Amiga native asm syntax by phx / English Amiga Board.
*
* Decompression of DEFLATE streams, as produced by zip/gzip/pkzip and
* specified in RFC 1951 "DEFLATE Compressed Data Format Specification".
*
* Usage: Optionally configure the OPT_xxx options below at build time;
* at run time 'bsr inflate' with arguments:
*    a4 = output buffer, a5 = input stream
*    a6 = *end* of temporary storage area (only if OPT_STORAGE_OFFSTACK)
* All register values (including arguments) are preserved.
*
* Space requirements: 638-930 bytes code; 2044-2940 bytes stack.
* (NB1. Above ranges are [No Optimisations]-[All Optimisations])
* (NB2. Stack space can be relocated to a separately-specified storage
*	area, see OPT_STORAGE_OFFSTACK below)
*
* Timings: With all Optimisation Options enabled (see below) this routine
* will decompress on a basic 7MHz 68000 at ~25kB/s. An AmigaDOS track of
* data (5.5kB) is processed in ~220ms. This is only fractionally slower than
* the track can be fetched from disk, hence there is scope for a
* decompressing loader to keep CPU and disk both at near 100% utilisation.
* 
* Written & released by Keir Fraser <keir.xen@gmail.com>
* 
* This is free and unencumbered software released into the public domain.
* See the file COPYING for more details, or visit <http://unlicense.org>.
*

* Optimisation Option #1:
* Avoid long Huffman-tree walks by indexing the first 8 bits of each codeword
* in a 256-entry lookup table. This shortens all walks by 8 steps and since
* the most common codes are less than 8 bits, most tree walks are avoided.
* Also pre-shifts selected symbols in the code->symbol table, ready to be used
* as indexes into further lookup tables.
* SPEEDUP: 41% (c.w. no Options); COST: 122 bytes code, 896 bytes stack */

*** rest of options removed by wl
*** code modifed for gst macro assembler by w. Lenerz

OPT_TABLE_LOOKUP	equ 1
	     
; Longest possible code.
MAX_CODE_LEN		equ 16

; (Maximum) alphabet sizes.
nr_codelen_symbols	equ 19
nr_litlen_symbols	equ 288
nr_distance_symbols	equ 32

; Alphabet-description stream for a static Huffman block (BTYPE=01b).
static_huffman_prefix:
	dc.b $ff,$5b,$00,$6c,$03,$36,$db
	dc.b $b6,$6d,$db,$b6,$6d,$db,$b6
	dc.b $cd,$db,$b6,$6d,$db,$b6,$6d
	dc.b $db,$a8,$6d,$ce,$8b,$6d,$3b


* Number of bytes required for code-lookup table/tree:
*  - 256 2-byte entries for the 8-bit lookup table
*  - Worst-case only 8 symbols decode directly in the table and all the rest
*    are in a tree hanging off one table entry. This tree requires
*    (nr_symbols-8)-1 internal 4-byte nodes.
LOOKUP_BYTES_CODELEN	equ 256*2+(nr_codelen_symbols-9)*4
LOOKUP_BYTES_LITLEN	equ 256*2+(nr_litlen_symbols-9)*4
LOOKUP_BYTES_DISTANCE	equ 256*2+(nr_distance_symbols-9)*4

	; a0 = len(), a1 = nodes(), d0 = nr_symbols
	; d1 = symbol beyond which all symbols get <<2
	; a2-a3 are scratched
build_code:
	movem.l d0-d7,-(sp)

	; Allocate space for bl_count()/next_code() array on stack.
	moveq	#(MAX_CODE_LEN+1)/2,d1
	moveq	#0,d2
.1.1:	move.l	d2,-(sp)
	dbf	d1,.1.1

	; Count occurrences of each code length into bl_count() array.
	subq.w	#1,d0
	move.w	d0,d1
	move.l	a0,a2		; a2 = &len(0)
.1.2:	move.b	(a2)+,d2	; d2 = len(i)
	add.b	d2,d2
	addq.w	#1,(sp,d2.w)	; bl_count(len(i))++
	dbf	d1,.1.2

	; Calculate next_code() start values for each code length.
	move.l	sp,a2		; a2 = bl_count() / next_code()
	moveq	#MAX_CODE_LEN-1,d1
	moveq	#0,d2		; d2 = code
	move.w	d2,(sp) 	; bl_count(0) = 0, ignore zero-length codes
.1.3:	 add.w	 (a2),d2
	add.w	d2,d2		; code = (code + bl_count(i-1)) << 1
	move.w	d2,(a2)+	; next_code(i) = code
	dbf	d1,.1.3

	; Create the Huffman-code lookup tree
	move.w	d0,d1
	moveq	#127,d4 	; d4 = next_node = 127
	move.l	a0,a2		; a2 = &len(0)
build_code_loop:
	moveq	#0,d5
	move.b	(a2)+,d5	; d5 = len(i) / *len++
	beq.s	build_code_next
	subq.w	#1,d5
	move.w	d5,d6
	add.w	d6,d6
	move.w	(sp,d6.w),d3	; d3 = code = next_code(len(i))++
	addq.w	#1,(sp,d6.w)
	move.w	d5,d6

	moveq	#0,d2
.2.1:	lsr.w	#1,d3
	roxl.w	#1,d2
	dbf	d6,.2.1 	  ; d5 = codelen-1; d2 = reversed code
	move.b	d2,d3
	add.w	d3,d3		; d3 = table offset
	move.w	d0,d6
	sub.w	d1,d6		; d6 = symbol
	cmp.w	(((MAX_CODE_LEN+1)/2)+1)*4+6(sp),d6 ; symbol > saved d1.w?
	bls.s	.2.2
	lsl.w	#2,d6		; symbol <<= 2 if so
.2.2:	cmp.b	#9-1,d5
	bpl.s	codelen_gt_8

codelen_le_8: ; codelen <= 8: leaf in table entry(s)
	lsl.w	#3,d6
	or.b	d5,d6		; d6 = (symbol<<3) | (codelen-1)
	moveq	#0,d2
	addq.b	#2,d5
	bset	d5,d2		; d2 = 1<<(codelen+1) (table step)
	move.w	d2,d7
	neg.w	d7
	and.w	#511,d7
	or.w	d7,d3		; d3 = last table offset
.3.1:	move.w	d6,(a1,d3.w)
	sub.w	d2,d3
	bpl	.3.1
	bra.s	build_code_next

codelen_gt_8: ; codelen > 8: requires a tree walk
	lsr.w	#8,d2
	subq.b	#8,d5		; Skip the first 8 bits of code
	lea	(a1,d3.w),a3	; pnode = table entry

.4.1:	  ; Walk through *pnode.
	move.w	(a3),d7 	; d3 = *pnode
	bne.s	.4.2
	; Link missing: Create a new internal node
	addq.w	#1,d4
	move.w	d4,d7
	bset	#15,d7
	move.w	d7,(a3) 	; *pnode = ++next_node | INTERNAL
.4.2:	  ; Take left or right branch depending on next code bit
	lsr.b	#1,d2
	addx.w	d7,d7
	add.w	d7,d7
	lea	(a1,d7.w),a3	; pnode = next_bit ? &node->r : &node->l
	dbf	d5,.4.1

	; Insert the current symbol as a new leaf node
	move.w	d6,(a3) 	; *pnode = sym
build_code_next:
	dbf	d1,build_code_loop

	lea	(((MAX_CODE_LEN+1)/2)+1)*4(sp),sp
	movem.l (sp)+,d0-d7
	rts

	; d5-d6/a5 = stream, a0 = tree
	; d0.w = result, d1.l = scratch
stream_next_symbol
	moveq	#0,d0	; 4
	moveq	#7,d1	; 4
	cmp.b	d1,d6	; 4
	bhi.s	.5.1	; 10
	; Less than 8 bits cached; grab another byte from the stream
	move.b	(a5)+,d0 ; (8)
	lsl.w	d6,d0	; (~14)
	or.w	d0,d5	; (4) s->cur |= *p++ << s->nr
	addq.b	#8,d6	; (4) s->nr += 8
	moveq	#0,d0	; (4)
.5.1	  ; Use next input byte as index into code lookup table
	move.b	d5,d0	; 4
	add.w	d0,d0	; 4
	move.w	(a0,d0.w),d0 ; 14
	bpl.s	.5.4	; 10 (taken)
	; Code is longer than 8 bits: do the remainder via a tree walk
	lsr.w	#8,d5
	subq.b	#8,d6		; consume 8 bits from the stream
.5.2   ; stream_next_bits(1), inlined & optimised
	subq.b	#1,d6		; 4 cy
	bpl.s	.5.3		; 10 cy (taken)
	move.b	(a5)+,d5	; (8 cy)
	moveq	#7,d6		; (4 cy)
.5.3	lsr.w	#1,d5		; 8 cy
	addx.w	d0,d0		; 4 cy
	add.w	d0,d0		; 4 cy
	move.w	(a0,d0.w),d0	; 14 cy
	bmi.s	.5.2		; 10 cy (taken); loop on INTERNAL flag
	bra.s	.5.5		; TOTAL LOOP CYCLES ~= 54
.5.4   ; Symbol found directly: consume bits and return symbol
	and.b	d0,d1	; 4
	addq.b	#1,d1	; 4
	lsr.w	d1,d5	; ~16 consume bits from the stream
	sub.b	d1,d6	; 4
	lsr.w	#3,d0	; 12  d0 = symbol
.5.5		       ; ~94 CYCLES TOTAL (+ 34)
	rts

     
	; d1.b = nr, d5-d6/a5 = stream (fetched_bits/nr_fetched_bits/inp)
	; d0.w = result
stream_next_bits
.6.1	moveq	#0,d0
	cmp.b	d1,d6
	bpl.s	.6.2		; while (s->nr < nr)
	move.b	(a5)+,d0
	lsl.l	d6,d0
	or.l	d0,d5		; s->cur |= *p++ << s->nr
	addq.b	#8,d6		; s->nr += 8
	bra.s	.6.1
.6.2	bset	d1,d0
	subq.w	#1,d0		; d0 = (1<<nr)-1
	and.w	d5,d0		; d0 = s->cur & ((1<<nr)-1)
	lsr.l	d1,d5		; s->cur >>= nr
	sub.b	d1,d6		; s->nr -= nr
	rts



	; d5-d6/a5 = stream, a4 = output
	; d0-d1 are scratched
uncompressed_block:
	; Push whole bytes back into input stream.
	lsr.w	#3,d6
	sub.w	d6,a5
	; Snap input stream up to byte boundary.
	moveq	#0,d5
	moveq	#0,d6
	; Read block header and copy LEN bytes.
	moveq	#16,d1
	bsr	stream_next_bits ; LEN
	addq.w	#2,a5		; skip NLEN
	subq.w	#1,d0		; d0.w = len-1 (for dbf)
.7.1:	move.b	(a5)+,(a4)+
	dbf	d0,.7.1
	rts


o_hdist 	equ 0
o_hlit		equ 2
o_lens		equ o_hlit+2
o_codelen_tree	equ o_lens+nr_litlen_symbols+nr_distance_symbols
; Lit/len and codelen lookup structures share space.
o_litlen_tree	equ o_codelen_tree
o_dist_tree	equ o_litlen_tree+LOOKUP_BYTES_LITLEN
o_stream	equ o_dist_tree+LOOKUP_BYTES_DISTANCE
o_frame 	equ o_stream+3*4
; Allow for BSR return address from decoder
o_mode		equ o_frame+4
o_dist_extra	equ o_mode+4
o_length_extra	equ o_dist_extra+30*4




	; d5-d6/a5 = stream, a4 = output
	; d0-d4,a0-a3 are scratched
static_huffman:
	movem.l d5-d6/a5,-(sp)
	moveq	#0,d5
	moveq	#0,d6
	lea	static_huffman_prefix(pc),a5
	move.w	#o_stream/4-2,d0
	bra.s	huffman

	; d5-d6/a5 = stream, a4 = output
	; d0-d4,a0-a3 are scratched
dynamic_huffman:
	; Allocate stack space for len() and node() arrays
	move.w	#o_frame/4-2,d0
huffman:
	moveq	#0,d1
.8.1:	  move.l  d1,-(sp)
	dbf	d0,.8.1
	; HLIT = stream_next_bits(5) + 257
	moveq	#5,d1
	bsr	stream_next_bits
	add.w	#257,d0
	move.w	d0,-(sp)
	; HDIST = stream_next_bits(5) + 1
	moveq	#5,d1
	bsr	stream_next_bits
	addq.w	#1,d0
	move.w	d0,-(sp)
	; HCLEN = stream_next_bits(4) + 4
	moveq	#4,d1
	bsr	stream_next_bits
	addq.w	#4-1,d0 	; -1 for dbf
	; Initialise len() array with code-length symbol code lengths
	lea	codelen_order(pc),a1
	lea	o_lens(sp),a0	; a0 = len()
	moveq	#0,d2
	move.w	d0,d3
.8.2:	  moveq   #3,d1
	bsr	stream_next_bits
	move.b	(a1)+,d2
	move.b	d0,(a0,d2.w)	; len(codelen_order(i++)) = next_bits(3)
	dbf	d3,.8.2
	; Build the codelen_tree
	lea	o_codelen_tree(sp),a1
	moveq	#nr_codelen_symbols,d0
	moveq	#127,d1 	; don't left-shift any symbols
	bsr	build_code	; build_code(codelen_tree)
	; Read the literal/length & distance code lengths
	move.w	o_hlit(sp),d2
	add.w	o_hdist(sp),d2
	subq.w	#1,d2		; d2 = hlit+hdist-1
	move.l	a0,a2		; a2 = len()
	move.l	a1,a0		; a0 = a1 = codelen_tree
c_loop:
	bsr	stream_next_symbol
	cmp.b	#16,d0
	bmi.s	.c_lit
	beq.s	.c_16
	cmp.b	#17,d0
	beq.s	.c_17
.c_18:					; 18: Repeat zero N times
	moveq	#7,d1
	bsr	stream_next_bits
	addq.w	#11-3,d0
	bra.s	.d.1
.c_17:					; 17: repeat zero N times
	moveq	#3,d1
	bsr	stream_next_bits
.d.1:	moveq	#0,d1
	bra.s	.d.2
.c_16:					; 16: repeat previous N times
	moveq	#2,d1
	bsr	stream_next_bits
	move.b	-1(a2),d1
.d.2:	addq.w	#3-1,d0
	sub.w	d0,d2
.d.3:	move.b	d1,(a2)+
	dbf	d0,.d.3
	bra.s	.d.4
.c_lit: 				; 0-16: Literal symbol
	move.b	d0,(a2)+
.d.4:	dbf	d2,c_loop
	; Build the lit/len and distance trees
	; Clear the codelen tree (shared space with lit/len tree).
	; NB. a0 = a1 = codelen_tree = litlen_tree
	moveq	#0,d0
	move.w	#LOOKUP_BYTES_CODELEN/4-1,d1
.d.5:	move.l	d0,(a0)+
	dbf	d1,.d.5
	lea	o_lens(sp),a0
	move.w	o_hlit(sp),d0
	move.w	#256,d1
	move.w	d1,d4		; left-shift symbols >127 (i.e., lengths)
	bsr	build_code	; build_code(litlen_tree)
	add.w	d0,a0
	lea	o_dist_tree(sp),a1
	move.w	o_hdist(sp),d0
	moveq	#0,d1		; left-shift all symbols (i.e., distances)
	bsr	build_code	; build_code(dist_tree)
	; Reinstate the main stream if we used the static prefix
	tst.l	o_stream+8(sp)
	beq.s	decode_loop
	movem.l o_stream(sp),d5-d6/a5
	; Now decode the compressed data stream up to EOB
decode_loop:

	lea	o_litlen_tree(sp),a0
	; START OF HOT LOOP
.9.1:
	bsr	stream_next_symbol ; litlen_sym
	cmp.w	d4,d0	 ;  4 cy (d4.w = 256)
	bpl.s  .9.2	  ;  8 cy
				; 0-255: Byte literal
	move.b	d0,(a4)+ ;  8 cy
	bra.s	.9.1	   ; 10 cy
	; END OF HOT LOOP -- 30 + ~108 + (34) = ~160 CYCLES
.done:	; 256: End-of-block: we're done
	lea	o_frame(sp),sp
	rts

.9.2:	beq	.done
	; 257+: <length,distance> pair
	lea	o_length_extra-257*4(sp),a2
	add.w	d0,a2
	move.w	(a2)+,d1
	bsr	stream_next_bits
	add.w	(a2),d0
	move.w	d0,d3		; d3 = cplen
	lea	o_dist_tree(sp),a0
	bsr	stream_next_symbol ; dist_sym
	lea	o_dist_extra(sp),a2
	add.w	d0,a2
	move.w	(a2)+,d1
	bsr	stream_next_bits
	add.w	(a2),d0 	; d0 = cpdst
	move.l	a4,a0
	sub.w	d0,a0		; a0 = outp - cpdst
	subq.w	#1,d3
.9.3:	move.b	(a0)+,(a4)+
	dbf	d3,.9.3
	bra	decode_loop

	; Build a base/extra-bits table on the stack
build_base_extrabits:
	move.l	(a7)+,a0
.a.1:
	move.w	d0,d3
	lsr.w	d4,d3
	subq.w	#1,d3
	bpl.s	.a.2
	moveq	#0,d3
.a.2:	moveq	#0,d1
	bset	d3,d1
	sub.w	d1,d2
	move.w	d2,-(sp)
	move.w	d3,-(sp)
	dbf	d0,.a.1
	jmp	(a0)

dispatch: ; Decoder dispatch table.
	dc.b uncompressed_block-uncompressed_block
	dc.b static_huffman-uncompressed_block
	dc.b dynamic_huffman-uncompressed_block

codelen_order: ; Order of code lengths for the code length alphabet.
	dc.b 16,17,18,0,8,7,9,6,10,5,11,4,12,3,13,2,14,1,15

	; a4 = output, a5 = input, all regs preserved
	; a6 = *end* of storage area (only if OPT_STORAGE_OFFSTACK)
unpack
inflate:

inflreg reg	d0-d6/a0-a5
	movem.l inflreg,-(sp)
	; Build the <length> base/extra-bits table
	move.l	#258,d2
	move.l	d2,-(sp)
	addq.w	#1,d2
	moveq	#27,d0
	moveq	#2,d4
	bsr	build_base_extrabits

	; Build the <distance> base/extra-bits table
	move.w	#32769,d2
	moveq	#29,d0
	moveq	#1,d4
	bsr	build_base_extrabits

	; Initialise the stream
	moveq	#0,d5		; d5 = stream: fetched data
	moveq	#0,d6		; d6 = stream: nr fetched bits

.b.1:				; Process a block: Grab the BTYPE|BFINAL 3-bit code
	moveq	#3,d1
	bsr	stream_next_bits
	move.l	d0,-(sp)
; Dispatch to the correct decoder for this block
	lsr.b	#1,d0
	move.b	dispatch(pc,d0.w),d0
	lea	uncompressed_block(pc),a0
	jsr	(a0,d0.w)
; Keep going until we see BFINAL=1
	move.l	(sp)+,d0
	lsr.b	#1,d0
	bcc	.b.1
; Pop the base/extra-bits lookup tables
	lea	(30+29)*4(sp),sp

	movem.l (sp)+,inflreg
	rts

	end
