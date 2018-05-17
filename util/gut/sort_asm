* Routine to sort a set of items.       v0.00   Dec 1986  J.R.Oakley  QJUMP
*
        section gen_util
*
        xdef    gu_sort
*+++
* This is a general purpose sort which will order any number of any
* type of item. The restrictions are that items must be of a known
* length, and in a contiguous regular table: items which do not
* conform to this scheme must be sorted by using a regular table of 
* pointers to them.
*
* Features include omitting unnecessary swaps, to allow sorting of one
* set of data on several fields, and ascending or descending ordering.
* Flexibility is achieved by the user providing the code to compare
* and swap two items.
*
*       Registers:
*               Entry                   Exit
*       D1.w    interval between items  preserved
*       D2.b    asc./desc. order (1/-1) preserved
*       D3.w    number of items         preserved
*       A2      pointer to user code    preserved
*       A3      pointer to item table   preserved
*
* The requirements on the user code are that 4(a2) should be the address
* of a routine that compares two items, and that (a2) should be the
* start address of a routine to exchange two items.
*
*       Registers:
*               Entry                   Exit
*       D0.b,CCR                        -1 if ((A0))<((A1)), 0 if equal, 1 if >
*       A0      pointer to table entry  preserved
*       A1      pointer to table entry  preserved
*       A2-A6   as on entry to sort
*
* All other registers should be preserved.
*---
gu_sort
        movem.l d0-d7/a0/a1,-(sp)
        subq.w  #1,d3                   ; last item number (0..n)
        ble.s   exit                    ; less than two items, don't sort
        moveq   #0,d4                   ; 
        bra.s   end_lp
exch_lp
        move.w  d4,d0                   ; set current item to insert
        mulu    d1,d0                   ; current item -> offset in table
        lea     0(a3,d0.l),a1           ; point A1 to entry in table
        moveq   #0,d5                   ; and lowest is item 0
        move.w  d4,d6                   ; highest to compare...
        subq.w  #1,d6                   ; ...is one less than current
search
        move.w  d5,d7                   ; middle element is...
        add.w   d6,d7                   ; ...in between the limits
        asr.w   #1,d7
        move.w  d7,d0                   ; convert to...
        mulu    d1,d0                   ; ...offset, to...
        lea     0(a3,d0.l),a0           ; ...absolute address
        jsr     4(a2)                   ; compare items
        cmp.b   d0,d2                   ; correct order?
        bne.s   chg_ll
        move.w  d7,d6
        subq.w  #1,d6                   ; change upper test limit
        bra.s   e_chg
chg_ll
        move.w  d7,d5
        addq.w  #1,d5                   ; change lower test limit
e_chg
        cmp.w   d5,d6                   ; crossed over yet?
        bge.s   search                  ; no, carry on searching
        sub.w   d4,d5                   ; there are this many items to shuffle
        neg.w   d5                      ; well, this many, actually
*
*       We now need to put the item addressed by (a1) up D5 places.
*
        bra.s   mv_end
mv_lp
        sub.w   d1,a1                   ; point to next item
        lea     0(a1,d1.w),a0           ; and back to current
        jsr     (a2)                    ; exchange two items
mv_end
        dbra    d5,mv_lp
end_lp
        addq.w  #1,d4
        cmp.w   d4,d3
        bge.s   exch_lp
exit
        movem.l (sp)+,d0-d7/a0/a1
        rts
*
        end
