* Remove linked list of heap entries    v0.01   J.R.Oakley  Dec 1986  QJUMP
*
        section util
*
        include 'dev8_keys_hdr'
        include 'dev8_wman_wstatus'
        include 'dev8_wman_wwork'
        include 'dev8_qram_keys'
*
        xref    qr_rechp
*
        xdef    fl_rmcks
        xdef    ut_mkrow
*+++
* Remove list of chunks, usually a directory made by UT_MKDIR
*
*       Registers:
*               Entry                           Exit
*       D4      pointer to first "chunk"        0
*       A0                                      smashed
*---
fl_rmcks
flr_rdlp
        tst.l   d4                      ; next chunk
        beq.s   flr_rcex                ; no more
        move.l  d4,a0                   ; point to it
        move.l  (a0),d4                 ; and to next one if any
        jsr     qr_rechp(pc)            ; remove it
        bra.s   flr_rdlp
flr_rcex
        rts
*+++
* Make the row list/objects out of the directory information, and
* set all the items to available.  The result is sorted according
* to a sort string in the status area.
*
*       Registers:
*               Entry                           Exit
*       D0-D3                                   smashed
*       D4      file count                      preserved
*       D5      2nd object offs. | status       preserved
*       A0      action routine on hit           preserved
*       A2      enough space for objects etc.   smashed
*       A3      pointer to file list            smashed
*       A5      status area for items           preserved
*---
ut_mkrow
mkrreg  reg     a1/a4
        movem.l mkrreg,-(sp)
        move.w  d4,d0                   ; this many files
        mulu    #wwm.rlen,d0            ; so this much space for the row list
        lea     0(a2,d0.l),a4           ; point to space for objects
        move.w  d4,d0                   ; smashable file count
        moveq   #0,d1                   ; remaining files in this chunk
        moveq   #0,d2                   ; first flag
        bra.s   mkr_lpe
*
*       Source file name object
*
gobj_lp
        move.l  a4,(a2)+                ; start of row
        move.l  #$020000e0,(a4)+        ; justify 2,0 : text : no selkey
        move.b  d5,0(a5,d2.w)           ; set according to ALL flag
        swap    d5
        lea     hdr_name(a3,d3.w),a1    ; point to source name
        move.l  a1,(a4)+                ; fill in pointer
        move.w  d2,(a4)+                ; and item number
        move.l  a0,(a4)+                ; fill in action routine
*
*       Destination file name object
*
        move.l  #$020000e0,(a4)+        ; justify 2,0 : text : no selkey
        lea     0(a3,d3.w),a1           ; point to file data
        add.w   d5,a1                   ; use whichever bit's selected
        swap    d5
        move.l  a1,(a4)+                ; fill in pointer
        move.w  d2,(a4)+                ; and item number
        move.l  a0,(a4)+                ; fill in action routine
        beq.s   mkr_serp                ; none, don't modify
        addq.l  #4,-4(a4)               ; there is one, it's 4 further on
mkr_serp
        move.l  a4,(a2)+                ; end of row pointer
*
        addq.w  #wss.ilen,d2            ; next slot in flag table
        add.w   #fl.dirln,d3            ; and in directory table
mkr_lpe
        subq.w  #1,d0                   ; one file fewer to deal with
        dbmi    d1,gobj_lp              ; do next file in this chunk
        bmi.s   mkr_ex                  ; stop if no more files 
        moveq   #9,d1                   ; 10 entries left
        moveq   #4,d3                   ; starting here
        move.l  (a3),a3                 ; in the next chunk
        bra.s   gobj_lp
mkr_ex
        movem.l (sp)+,mkrreg
        rts
*
        end

