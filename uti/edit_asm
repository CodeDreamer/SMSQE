; standard editor

        section utility

        include dev8_keys_k
        include dev8_keys_err
        include dev8_keys_qdos_io
        include dev8_uti_edit_data

        xdef    ut_edit,prt_text,pos_curs

;+++
; Standard text editor.
;
;               Entry                           Exit
;       A0      channel ID                      preserved
;       A3      workspace                       preserved
;---
ut_edit
        moveq   #iow.chrq,d0
        lea     ed_buff(a3),a1          ; enquiry block
        bsr.s   do_io
        move.w  ed_lines(a3),d0
        mulu    ed_colms(a3),d0         ; number of characters to set
        lea     ed_text(a3),a1          ; the text
        bra.s   clr_text
clr_loop
        move.b  #' ',(a1)+
clr_text
        dbra    d0,clr_loop
        move.l  edv_init(a3),d0         ; init routine supplied?
        beq.s   no_init
        move.l  d0,a1
        jsr     (a1)
        bmi.s   ed_abort
no_init
        bsr.s   prt_text                ; print out text ..

;       now we enter the main edit-loop
ed_loop
        moveq   #iob.fbyt,d0
        bsr.s   do_io                   ; fetch character
        cmp.b   #k.esc,d1               ; escaped?
        beq.s   ed_quit
        moveq   #0,d7                   ; no options
        move.l  edv_char(a3),d0         ; special character handling?
        beq.s   char_nsp                ; no!
        move.l  d0,a1
        moveq   #0,d0
        jsr     (a1)                    ; call it
        bmi.s   ed_abort
        move.l  d0,d7                   ; keep return flags
char_nsp
        btst    #edk..ign,d7            ; ignore current character?
        bne.s   ignore
        bsr.s   ed_char                 ; now edit character
ignore
        btst    #edk..rds,d7            ; redraw screen?
        beq.s   no_redrs                ; no!
        bsr.s   prt_text                ; print text
        bra     ed_loop                 ; .. and back into loop
no_redrs
        btst    #edk..rdl,d7            ; redraw current line only?
        beq.s   no_redrl                ; no!
        bsr.s   prt_clin                ; print this line
        bra     ed_loop
no_redrl
        btst    #edk..cur,d7            ; set cursor only?
        beq     ed_loop                 ; no, thats it
        bsr.s   pos_curs
        bra     ed_loop

ed_quit
        move.l  edv_exit(a3),d0         ; exit routine supplied?
        beq.s   ed_abort                ; no!
        move.l  d0,a1
        jsr     (a1)                    ; call it
ed_abort
        rts

do_io
        moveq   #forever,d3
        trap    #do.io
        tst.l   d0
        rts

;+++
; Print whole text.
;---
prt_text
        moveq   #iow.clra,d0
        bsr     do_io                   ; clear all first
        moveq   #0,d1                   ; line count
prt_tlp
        bsr.s   prt_line                ; print this line
        addq.w  #1,d1                   ; next one
        cmp.w   ed_lines(a3),d1
        bcs.s   prt_tlp
;+++
; Position and enable cursor.
;---
pos_curs
        move.w  ed_curcl(a3),d1
        move.w  ed_curln(a3),d2
        moveq   #iow.scur,d0            ; set cursor to pre-defined position
        bsr     do_io
        moveq   #iow.ecur,d0
        bra     do_io                   ; and enable it

;+++
; Print current cursor line and position cursor.
;---
prt_clin
        move.w  ed_curln(a3),d1
        bsr.s   prt_line
        bra     pos_curs

;+++
; Print one line (d1).
;---
prt_line
        move.w  d1,-(sp)                ; keep line number
        move.w  d1,d2
        moveq   #0,d1
        moveq   #iow.scur,d0
        bsr     do_io                   ; set cursor position
        move.w  ed_colms(a3),d0
        move.w  d0,d2                   ; line length
        mulu    (sp),d0                 ; here starts our line in text buffer
        lea     ed_text(a3,d0.w),a1     ; now absolute!
        subq.w  #1,d2
        move.w  #iob.smul,d0
        bsr     do_io                   ; write out line
        move.w  (sp)+,d1
        rts

ed_char
        move.w  ed_colms(a3),d0
        move.w  ed_curln(a3),d4
        move.w  ed_curcl(a3),d5
        mulu    d4,d0                   ; get line
        add.w   d5,d0                   ; and add column
        lea     ed_text(a3,d0.w),a1     ; our character

        cmp.b   #k.space,d1             ; everything below space ..
        bcs.s   ed_ctllo
        cmp.b   #k.maxch,d1             ; .. and cursor movement needs ..
        bhi.s   ed_ctlhi                ; special routines
        addq.w  #1,d5
        cmp.w   ed_colms(a3),d5
        beq.s   ed_rts
        subq.w  #1,d5
        move.b  d1,(a1)                 ; insert character (assume overwrite)
        moveq   #iob.sbyt,d0
        bsr     do_io                   ; print character
        bra.s   ed_right                ; and move on to next one

ed_ctllo
        cmp.b   #k.enter,d1
        beq.s   ed_enter
ed_rts
        rts
ed_rtse
        moveq   #err.orng,d0
        rts

ed_ctlhi
        moveq   #0,d2
        move.b  d1,d2
        sub.b   #k.curs,d2              ; make an index into table
        cmp.b   #$20,d2
        bcc     ed_rts                  ; no cursor movement!
        add.l   d2,d2
        lea     curs_tab,a2
        add.w   (a2,d2.w),a2
        jmp     (a2)

ed_enter
        addq.w  #1,d4
        cmp.w   ed_lines(a3),d4         ; bottom reached?
        beq     ed_scroll               ; yes!
        clr.w   ed_curcl(a3)            ; start at ..
        addq.w  #1,ed_curln(a3)         ; .. next line
        bra     pos_curs

ed_right
        addq.w  #1,d5
        cmp.w   ed_colms(a3),d5         ; already at right border?
        beq.s   ed_rtse
        addq.w  #1,ed_curcl(a3)         ; one character right
        bra     pos_curs

ed_aright
        move.w  ed_colms(a3),d2
        subq.w  #1,d2
        move.w  d2,ed_curcl(a3)         ; move to end of line
        bra     pos_curs

ed_acleft
        clr.w   ed_curcl(a3)
        mulu    ed_colms(a3),d4
        lea     ed_text(a3,d4.w),a1     ; get start of line .. and into acright
ed_acright
        move.w  ed_curcl(a3),d2         ; delete to end of line
        addq.w  #1,d2
ed_dlrlp
        move.b  #' ',(a1)+              ; fill all with spaces
        addq.w  #1,d2
        cmp.w   ed_colms(a3),d2
        bne.s   ed_dlrlp
        bra     prt_clin

ed_left
        tst.w   d5                      ; already at left border?
        beq.s   ed_rtse
        subq.w  #1,ed_curcl(a3)
        bra     pos_curs

ed_aleft
        clr.w   ed_curcl(a3)            ; move to start of line
        bra     pos_curs

ed_cleft
        bsr     ed_left                 ; delete left (BACKSPACE)
        bne     ed_rts                  ; can't move left, so delete is impossible
        subq.l  #1,a1
ed_cright
        move.w  ed_curcl(a3),d2         ; copy all one character left
        addq.w  #1,d2
ed_crcp
        move.b  1(a1),(a1)+
        addq.w  #1,d2
        cmp.w   ed_colms(a3),d2
        bne     ed_crcp
        move.b  #' ',(a1)               ; and add a space
        bra     prt_clin

ed_up
        tst.w   d4                      ; already at top of window?
        beq.s   ed_scroll
        subq.w  #1,ed_curln(a3)         ; up a line
        bra     pos_curs

ed_down
        addq.w  #1,d4
        cmp.w   ed_lines(a3),d4         ; bottom of window reached?
        beq.s   ed_scroll
        addq.w  #1,ed_curln(a3)         ; down a line
        bra     pos_curs

ed_aup
ed_saup
ed_adown
ed_sadown
ed_sdown
ed_sup
ed_scroll
        move.l  edv_scrl(a3),d0
        beq     ed_rts
        move.l  d0,a1
        jsr     (a1)
        bmi     ed_abort
        btst    #edk..rds,d0            ; redraw screen?
        beq     ed_rts
        bra     prt_text

curs_tab
        dc.w    ed_left-curs_tab        ; $C0
        dc.w    ed_aleft-curs_tab
        dc.w    ed_cleft-curs_tab
        dc.w    ed_acleft-curs_tab
        dc.w    ed_rts-curs_tab
        dc.w    ed_rts-curs_tab
        dc.w    ed_rts-curs_tab
        dc.w    ed_rts-curs_tab

        dc.w    ed_right-curs_tab       ; $C8
        dc.w    ed_aright-curs_tab
        dc.w    ed_cright-curs_tab
        dc.w    ed_acright-curs_tab
        dc.w    ed_rts-curs_tab
        dc.w    ed_rts-curs_tab
        dc.w    ed_rts-curs_tab
        dc.w    ed_rts-curs_tab

        dc.w    ed_up-curs_tab          ; $D0
        dc.w    ed_aup-curs_tab
        dc.w    ed_rts-curs_tab
        dc.w    ed_rts-curs_tab
        dc.w    ed_sup-curs_tab
        dc.w    ed_saup-curs_tab
        dc.w    ed_rts-curs_tab
        dc.w    ed_rts-curs_tab

        dc.w    ed_down-curs_tab        ; $D8
        dc.w    ed_adown-curs_tab
        dc.w    ed_rts-curs_tab
        dc.w    ed_rts-curs_tab
        dc.w    ed_sdown-curs_tab
        dc.w    ed_sadown-curs_tab
        dc.w    ed_rts-curs_tab
        dc.w    ed_rts-curs_tab

;+++
; EDIT init routine
;
;               Entry                           Exit
;       D0                                      error code
;       D1-D3                                   smashed
;       A0      channel ID                      preserved
;       A1-A2                                   smashed
;       A3      workspace                       preserved
;---

;+++
; EDIT character handling routine
;
;               Entry                           Exit
;       D0                                      error code or flag (bits set):
;                                                0 = do not treat character
;                                                5 = set cursor (explicitly)
;                                                6 = redraw cursor line
;                                                7 = redraw whole screen
;       D1      character                       character (maybe modified)
;       D2-D3                                   smashed
;       A0      channel ID                      preserved
;       A1-A2                                   smashed
;       A3      workspace                       preserved
;---

;+++
; EDIT scroll handler
;
;               Entry                           Exit
;       D0                                      error code or flag (bits set):
;                                                7 = redraw whole screen
;       D1      character                       smashed
;       D2-D3                                   smashed
;       A0      channel ID                      preserved
;       A1-A2                                   smashed
;       A3      workspace                       preserved
;---
        end
