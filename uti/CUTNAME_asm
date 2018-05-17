; Cut off device name from file name

        section utility

        xdef    ut_cutnm

;+++
; Cut off device name from file name
; If the device name is preceeded by 'Nx_', this is also removed
;
;               Entry                           Exit
;       d0                                      smashed
;       d5-d6                                   smashed
;       a2      file name                       preserved
;---
ut_cutnm
        move.w  (a2),d0                 ; length of file name
        beq.s   cut_deve                ; equal length (true?)
        moveq   #0,d6                   ; prepare characters to cut
        cmp.b   #'n',2(a2)              ; Net file server before drive?
        beq.s   cut_net
        cmp.b   #'N',2(a2)
        bne.s   cut_nonet
cut_net
        cmp.b   #'0',3(a2)
        bls.s   cut_nonet
        cmp.b   #'8',3(a2)
        bhi.s   cut_nonet
        moveq   #3,d6                   ; cut off net file server name
cut_nonet
        cmp.w   d6,d0                   ; longer than real name?
        bls.s   cut_deve                ; yes, must not be!
        cmp.b   #'_',2(a2,d6.w)         ; now we have to find next '_'
        beq.s   cut_dvus
        addq.w  #1,d6                   ; compare next character
        bra.s   cut_nonet
cut_dvus
        addq.w  #1,d6                   ; that's the real start of our name
        sub.w   d6,(a2)                 ; update length of name
        moveq   #0,d5
        sub.w   d6,d0
        beq.s   cut_deve                ; nothing left!
cut_cplp
        move.b  2(a2,d6.w),2(a2,d5.w)   ; move characters of name down ...
        addq.w  #1,d6                   ; ... to get them behind ...
        addq.w  #1,d5                   ; the name length
        subq.w  #1,d0
        bne.s   cut_cplp
cut_deve                                ; that's all!
        rts

        end
