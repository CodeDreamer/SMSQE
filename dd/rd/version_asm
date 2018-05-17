; RAM DISK Version

        section version

        xdef    rd_vmess
        xdef    rd_vmend
        xdef    rd_vers

; V2.02   Sub-directories added.
;
; V2.03   FS.MKDIR corrected.
;
; V2.04   Should have been the same as V2.05, but some 2.04 (non released
;         versions) were the same as V2.03.
;
; V2.05   Underscore ignored at end of directory name.
;         Length of ROOT directory corrected.
;         New file has update date set even if not written to.
;
; V2.06   Delete from static RAM disk corrected.
;
; V2.07   Bug handling more than 127 files fixed.
;
; V2.08   Revised for FD changes to IOU.
;
; V2.09   Corrects occupancy error in V2.08.
;
; V2.10   IOF.XINF now returns block of 64 bytes NOT $64 (96) bytes
;
; V2.11   Make_dir error with multiple files open fixed.
;
rd_vers equ     '2.11'
rd_vmess
        dc.w    'RAM disk V'
        dc.l    rd_vers
        dc.b    ' ',$a
rd_vmend
        ds.w    0
        end
